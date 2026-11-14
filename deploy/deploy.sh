#!/usr/bin/env bash
set -Eeuo pipefail
umask 077
export DOCKER_HOST=unix:///run/airlines-docker/docker.sock

image=${1:?image digest required}
commit=${2:?commit required}
workflow_url=${3:?workflow URL required}
[[ "$image" =~ ^ghcr\.io/is-courswork/core@sha256:[a-f0-9]{64}$ ]] || exit 2
[[ "$commit" =~ ^[a-f0-9]{40}$ ]] || exit 2
[[ "$workflow_url" =~ ^https://github\.com/is-courswork/core/actions/runs/[0-9]+$ ]] || exit 2

base=/opt/airlines
exec 9>"$base/deploy.lock"
flock -w 600 9
release="$base/releases/$commit"
previous=$(readlink -f "$base/current" || true)
# The archive is uploaded by the same successful workflow run as the image.
mkdir -p "$release"
tar -xzf "$base/incoming/$commit.tar.gz" -C "$release" --no-same-owner
chmod -R a+rX "$release"
printf 'CORE_IMAGE=%s\n' "$image" > "$release/.release.env"
python3 - "$release/static/version.json" "$commit" "$workflow_url" <<'PY'
import datetime, json, sys
with open(sys.argv[1], 'w') as f:
    json.dump({'commit': sys.argv[2], 'workflow_url': sys.argv[3],
               'deployed_at': datetime.datetime.now(datetime.timezone.utc).isoformat()}, f)
PY
chmod 644 "$release/static/version.json"

compose() {
    local dir=$1
    shift
    docker compose --project-name airlines --env-file "$base/shared/.env" \
      --env-file "$dir/.release.env" -f "$dir/compose.yaml" "$@"
}

rollback() {
    local status=$?
    trap - ERR
    echo "Deployment failed; recent core logs:"
    compose "$release" logs --tail 60 core || true
    if [[ -n "$previous" && -f "$previous/.release.env" ]]; then
        echo "Restoring previous release: $(basename "$previous")"
        compose "$previous" up -d --remove-orphans || true
    fi
    echo 'Database migrations are not automatically reverted.'
    exit "$status"
}
trap rollback ERR

compose "$release" config --quiet
compose "$release" pull
compose "$release" up -d --remove-orphans --wait --wait-timeout 240
healthy=false
for attempt in $(seq 1 60); do
    if compose "$release" exec -T caddy wget -q -O - \
       http://core:8080/api/core/actuator/health | grep -q '"status":"UP"'; then
        healthy=true
        break
    fi
    sleep 3
done
[[ "$healthy" == true ]]

ln -sfn "$release" "$base/current.next"
mv -Tf "$base/current.next" "$base/current"
printf '%s\n' "$image" > "$base/deployed-image"
rm -f "$base/incoming/$commit.tar.gz"
echo "Deployed $commit ($image)"
compose "$release" ps
