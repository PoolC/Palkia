BEGIN;

TRUNCATE TABLE
  attendance,
  session_file_list,
  session,
  activity_file_list,
  activity_members,
  activity_tag,
  activity,
  post_file_list,
  comment,
  post,
  project_members,
  project,
  book_tags,
  book,
  roles,
  member,
  poolc
RESTART IDENTITY CASCADE;

INSERT INTO poolc (id, president_name, phone_number, location, location_url, introduction, main_image_url, is_subscription_period, apply_uri)
VALUES (
  1,
  '개발 회장',
  '010-0000-0000',
  '숭실대학교 정보과학관 개발 동아리방',
  'https://map.naver.com/',
  '로컬 개발 환경용 PoolC 소개 데이터입니다. 홈페이지 리팩토링 중 빈 DB 오류를 피하기 위한 seed입니다.',
  'https://poolc.org/logo192.png',
  true,
  'https://forms.gle/dev-poolc-apply'
);

INSERT INTO member (uuid, created_at, updated_at, department, email, introduction, is_excepted, login_id, name, password_hash, phone_number, profile_image_url, student_id)
VALUES
  ('00000000-0000-0000-0000-000000000001', now(), now(), '컴퓨터학부', 'admin@poolc.dev', '로컬 개발용 임원진 계정입니다.', false, 'admin', '개발 관리자', '$argon2id$v=19$m=4096,t=3,p=1$sSta6jFDxAkJwmn6fqv6Hg$Srgwhr4dm2whPhuIbVx3HhsGkNeTM4pPtHHbYX03w+I', '010-0000-0001', '', '20260001'),
  ('00000000-0000-0000-0000-000000000002', now(), now(), '소프트웨어학부', 'president@poolc.dev', '로컬 개발용 회장 계정입니다.', false, 'president', '개발 회장', '$argon2id$v=19$m=4096,t=3,p=1$sSta6jFDxAkJwmn6fqv6Hg$Srgwhr4dm2whPhuIbVx3HhsGkNeTM4pPtHHbYX03w+I', '010-0000-0002', '', '20260002'),
  ('00000000-0000-0000-0000-000000000003', now(), now(), '컴퓨터학부', 'member1@poolc.dev', '세미나와 프로젝트에 참여하는 개발용 일반 회원입니다.', false, 'member1', '개발 회원1', '$argon2id$v=19$m=4096,t=3,p=1$sSta6jFDxAkJwmn6fqv6Hg$Srgwhr4dm2whPhuIbVx3HhsGkNeTM4pPtHHbYX03w+I', '010-0000-0003', '', '20260003'),
  ('00000000-0000-0000-0000-000000000004', now(), now(), 'AI융합학부', 'member2@poolc.dev', '백엔드와 인프라에 관심 있는 개발용 일반 회원입니다.', false, 'member2', '개발 회원2', '$argon2id$v=19$m=4096,t=3,p=1$sSta6jFDxAkJwmn6fqv6Hg$Srgwhr4dm2whPhuIbVx3HhsGkNeTM4pPtHHbYX03w+I', '010-0000-0004', '', '20260004'),
  ('00000000-0000-0000-0000-000000000005', now(), now(), '글로벌미디어학부', 'member3@poolc.dev', '디자인과 프론트엔드에 관심 있는 개발용 일반 회원입니다.', false, 'member3', '개발 회원3', '$argon2id$v=19$m=4096,t=3,p=1$sSta6jFDxAkJwmn6fqv6Hg$Srgwhr4dm2whPhuIbVx3HhsGkNeTM4pPtHHbYX03w+I', '010-0000-0005', '', '20260005'),
  ('00000000-0000-0000-0000-000000000006', now(), now(), '컴퓨터학부', 'pending@poolc.dev', '승인 전 회원 화면 확인용 계정입니다.', false, 'pending', '승인대기 회원', '$argon2id$v=19$m=4096,t=3,p=1$sSta6jFDxAkJwmn6fqv6Hg$Srgwhr4dm2whPhuIbVx3HhsGkNeTM4pPtHHbYX03w+I', '010-0000-0006', '', '20260006');

INSERT INTO roles (member_uuid, roles) VALUES
  ('00000000-0000-0000-0000-000000000001', 'ADMIN'),
  ('00000000-0000-0000-0000-000000000001', 'MEMBER'),
  ('00000000-0000-0000-0000-000000000002', 'ADMIN'),
  ('00000000-0000-0000-0000-000000000002', 'MEMBER'),
  ('00000000-0000-0000-0000-000000000003', 'MEMBER'),
  ('00000000-0000-0000-0000-000000000004', 'MEMBER'),
  ('00000000-0000-0000-0000-000000000005', 'MEMBER'),
  ('00000000-0000-0000-0000-000000000006', 'UNACCEPTED');

INSERT INTO post (id, created_at, updated_at, anonymous, board_type, body, comment_count, is_deleted, is_question, like_count, post_type, scrap_count, author_uuid, title)
VALUES
  (1, now() - interval '10 days', now() - interval '10 days', false, 'NOTICE', '2026-02 학기 PoolC 활동 안내입니다. 세미나, 프로젝트, PKS 사용 신청은 홈페이지에서 확인합니다.', 0, false, false, 3, 1, 1, '00000000-0000-0000-0000-000000000001', '2026-02 학기 활동 안내'),
  (2, now() - interval '7 days', now() - interval '7 days', false, 'NOTICE', '운영진 자료 보관과 PoolC Forms 리팩토링 검토를 위한 개발 공지입니다.', 0, false, false, 1, 1, 0, '00000000-0000-0000-0000-000000000002', '홈페이지 리팩토링 개발 공지'),
  (3, now() - interval '5 days', now() - interval '5 days', false, 'FREE', '로컬 개발 DB에서 자유게시판 목록과 상세 화면 확인용 글입니다.', 0, false, false, 2, 1, 0, '00000000-0000-0000-0000-000000000003', '더미 자유게시글'),
  (4, now() - interval '3 days', now() - interval '3 days', false, 'PROJECT', 'PKS 위에 팀별 웹서비스를 배포하는 흐름을 실험합니다.', 0, false, false, 4, 1, 2, '00000000-0000-0000-0000-000000000004', 'PKS 배포 실험 프로젝트 모집'),
  (5, now() - interval '2 days', now() - interval '2 days', false, 'EXTERNAL', '행사, 공모전, 해커톤, 외부 활동처럼 PoolC 구성원에게 도움이 되는 대외활동 정보를 공유합니다.', 0, false, false, 0, 1, 0, '00000000-0000-0000-0000-000000000003', '대외활동 게시판 안내'),
  (6, now() - interval '1 day', now() - interval '1 day', false, 'CAREER', '채용, 인턴, 커리어 설명회 정보를 공유합니다.', 0, false, false, 0, 1, 0, '00000000-0000-0000-0000-000000000003', '채용 게시판 안내'),
  (7, now(), now(), false, 'STAFF', '운영진 내부 회의록, 정산, 명단, 자료 보관 관련 글을 정리하는 게시판입니다.', 0, false, false, 0, 1, 0, '00000000-0000-0000-0000-000000000001', '운영진 게시판 안내');

INSERT INTO activity (id, available, capacity, class_hour, description, hour, is_seminar, start_date, title, host)
VALUES
  (1, true, 30, '매주 화 19:00-21:00', 'React/Vite 기반 PoolC 홈페이지 리팩토링 세미나입니다.', 12, true, DATE '2026-09-03', 'PoolC Frontend Refactoring Seminar', '00000000-0000-0000-0000-000000000001'),
  (2, true, 20, '매주 목 19:00-21:00', 'Spring Boot API와 PostgreSQL 설계 개선 세미나입니다.', 10, true, DATE '2026-09-05', 'PoolC Backend Refactoring Seminar', '00000000-0000-0000-0000-000000000002'),
  (3, false, 8, '격주 토 14:00-17:00', 'PKS/Gitea/GitOps를 이용한 팀 웹서비스 배포 프로젝트입니다.', 16, false, DATE '2026-09-14', 'PKS Platform Project', '00000000-0000-0000-0000-000000000001');

INSERT INTO activity_members (activity_id, member_login_id) VALUES
  (1, 'member1'),
  (1, 'member2'),
  (2, 'member2'),
  (2, 'member3');

INSERT INTO activity_tag (id, activity_id, content) VALUES
  (1, 1, 'frontend'),
  (2, 1, 'react'),
  (3, 2, 'backend'),
  (4, 2, 'spring'),
  (5, 3, 'pks');

INSERT INTO session (id, activity_id, date, description, hour, session_number) VALUES
  (1, 1, DATE '2026-09-03', '개발 환경 구성과 코드베이스 읽기', 2, 1),
  (2, 1, DATE '2026-09-10', '게시판/세미나 UI 리팩토링', 2, 2),
  (3, 2, DATE '2026-09-05', '도메인 모델과 권한 구조 읽기', 2, 1),
  (4, 2, DATE '2026-09-12', 'Forms API 설계', 2, 2);

INSERT INTO project (id, created_at, updated_at, body, description, duration, genre, name, thumbnail_url)
VALUES
  (1, now() - interval '20 days', now() - interval '20 days', 'PoolC Forms MVP: 신청폼 생성, 응답 수집, CSV export.', 'Google Forms를 대체하는 PoolC 내부 폼 시스템', '2026-02', 'Web', 'PoolC Forms', 'https://poolc.org/logo192.png'),
  (2, now() - interval '15 days', now() - interval '15 days', 'PKS 기반 팀별 웹서비스 배포 포털과 Gitea 연동 실험.', '동아리 팀 프로젝트 배포 플랫폼', '2026-02', 'Infra', 'PKS Developer Platform', 'https://poolc.org/logo192.png');

INSERT INTO project_members (project_id, member_loginids) VALUES
  (1, 'admin'),
  (1, 'member1'),
  (2, 'president'),
  (2, 'member2');

INSERT INTO book (id, created_at, updated_at, author, description, discount, donor, image_url, isbn, link, published_date, publisher, rent_date, status, title, renter)
VALUES
  (1, now(), now(), 'Martin Fowler', '리팩토링 기본기를 확인하기 위한 개발용 도서 데이터입니다.', 0, 'PoolC', '', '9788966263493', 'https://example.com/refactoring', '2020', '한빛미디어', NULL, 'AVAILABLE', 'Refactoring', NULL),
  (2, now(), now(), 'Robert C. Martin', '클린 코드 관련 개발용 도서 데이터입니다.', 0, 'PoolC', '', '9788966260959', 'https://example.com/clean-code', '2013', '인사이트', NULL, 'AVAILABLE', 'Clean Code', NULL);

SELECT setval('post_seq', 100, true);
SELECT setval('activity_seq', 100, true);
SELECT setval('activity_tag_seq', 100, true);
SELECT setval('session_seq', 100, true);
SELECT setval('project_seq', 100, true);
SELECT setval('book_seq', 100, true);

COMMIT;
