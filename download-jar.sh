curl -s https://api.github.com/repos/SHvatov/build-stats/releases/latest \
| grep "browser_download_url.*.jar" \
| cut -d : -f 2,3 \
| tr -d \" \
| wget -qi -