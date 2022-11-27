delete
from databasechangelog
where id in (select id from databasechangelog);

drop table if exists gh_tag_build_link cascade;
drop table if exists gh_build_ntf_pref cascade;
drop table if exists gh_user_repo_link cascade;
drop table if exists gh_tag cascade;
drop table if exists gh_build cascade;
drop table if exists gh_user cascade;
drop table if exists gh_ntf_types cascade;
drop table if exists gh_repo_build_stats cascade;
drop table if exists gh_repository cascade;
drop table if exists gh_build_stage cascade;
drop table if exists gh_build_stage_history cascade;
drop sequence if exists hibernate_sequence cascade;