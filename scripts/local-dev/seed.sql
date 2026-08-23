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
  (3, false, 8, '격주 토 14:00-17:00', 'PKS/Gitea/GitOps를 이용한 팀 웹서비스 배포 프로젝트입니다.', 16, false, DATE '2026-09-14', 'PKS Platform Project', '00000000-0000-0000-0000-000000000001'),
  (4, true, 16, '매주 월 19:00-21:00', 'TypeScript와 React 기본기를 다지는 2026-1 개발용 세미나입니다.', 10, true, DATE '2026-03-10', 'TypeScript Basics Seminar', '00000000-0000-0000-0000-000000000001'),
  (5, false, 12, '격주 수 20:00-22:00', '운영진이 진행했던 알고리즘 문제 풀이 스터디 더미 데이터입니다.', 12, false, DATE '2026-05-13', 'Algorithm Study', '00000000-0000-0000-0000-000000000002'),
  (6, true, 10, '매주 토 13:00-15:00', '동아리 홈페이지 디자인 시스템을 정리하는 2026-1 스터디입니다.', 8, false, DATE '2026-07-04', 'PoolC Design System Study', '00000000-0000-0000-0000-000000000005'),
  (7, false, 25, '매주 화 18:30-20:30', '웹 백엔드 입문자를 위한 2025-2 Spring 세미나 더미 데이터입니다.', 12, true, DATE '2025-09-09', 'Spring Boot Intro Seminar', '00000000-0000-0000-0000-000000000002'),
  (8, true, 18, '매주 금 19:00-21:00', '게임잼 준비를 위한 Unity 기초 스터디 더미 데이터입니다.', 14, false, DATE '2025-10-17', 'Unity Game Jam Study', '00000000-0000-0000-0000-000000000003'),
  (9, false, 14, '격주 일 15:00-18:00', 'Linux, Docker, Kubernetes 배포 흐름을 실습하는 인프라 세미나입니다.', 18, true, DATE '2026-01-11', 'DevOps Deployment Seminar', '00000000-0000-0000-0000-000000000001'),
  (10, true, 18, '매주 월 18:30-20:30', 'Python으로 데이터 처리와 자동화 기본기를 익히는 세미나입니다.', 10, true, DATE '2026-09-08', 'Python Automation Seminar', '00000000-0000-0000-0000-000000000003'),
  (11, true, 12, '매주 월 20:00-22:00', 'Next.js App Router와 UI 컴포넌트 설계를 실습하는 스터디입니다.', 12, false, DATE '2026-09-15', 'Next.js UI Study', '00000000-0000-0000-0000-000000000005'),
  (12, false, 10, '격주 화 19:00-21:00', 'C++ 기초 문법과 알고리즘 문제 풀이를 함께 진행합니다.', 14, true, DATE '2026-09-22', 'C++ Algorithm Seminar', '00000000-0000-0000-0000-000000000002'),
  (13, true, 16, '매주 수 18:00-20:00', 'Git, GitHub, Gitea를 활용한 협업 워크플로우를 연습합니다.', 8, true, DATE '2026-10-01', 'Git Collaboration Seminar', '00000000-0000-0000-0000-000000000001'),
  (14, true, 8, '매주 수 20:00-22:00', '운영체제 주요 개념을 발표와 토론으로 정리하는 스터디입니다.', 12, false, DATE '2026-10-08', 'Operating System Study', '00000000-0000-0000-0000-000000000004'),
  (15, false, 20, '매주 목 18:30-20:30', 'Figma와 CSS를 이용해 PoolC 페이지 컴포넌트를 개선합니다.', 10, true, DATE '2026-10-15', 'Product UI Workshop', '00000000-0000-0000-0000-000000000005'),
  (16, true, 14, '격주 목 20:00-22:00', 'SQL 쿼리 작성과 데이터 모델링 기초를 다지는 세미나입니다.', 10, true, DATE '2026-10-22', 'Database Basics Seminar', '00000000-0000-0000-0000-000000000002'),
  (17, true, 9, '매주 금 19:00-21:00', '개인 블로그와 동아리 팀 페이지를 직접 배포하는 스터디입니다.', 8, false, DATE '2026-10-29', 'Personal Website Deploy Study', '00000000-0000-0000-0000-000000000001'),
  (18, false, 15, '격주 토 10:00-12:00', 'HTTP, TLS, DNS 등 웹 네트워크 기본기를 실습합니다.', 10, true, DATE '2026-11-05', 'Web Network Seminar', '00000000-0000-0000-0000-000000000003'),
  (19, true, 11, '격주 토 13:00-16:00', '게임잼을 위한 기획, 프로토타이핑, 발표 준비를 진행합니다.', 16, false, DATE '2026-11-12', 'Game Jam Prep Study', '00000000-0000-0000-0000-000000000004'),
  (20, true, 13, '매주 일 13:00-15:00', 'Spring Security와 인증/인가 흐름을 읽고 구현합니다.', 12, true, DATE '2026-11-19', 'Auth Backend Seminar', '00000000-0000-0000-0000-000000000002'),
  (21, false, 6, '매주 일 15:00-17:00', '소규모 팀별 코드 리뷰와 리팩토링 연습을 진행합니다.', 8, false, DATE '2026-11-26', 'Clean Code Reading Study', '00000000-0000-0000-0000-000000000001'),
  (22, true, 24, '월 1회 토 14:00-17:00', '동아리 프로젝트 배포 사례를 공유하는 오픈 세미나입니다.', 6, true, DATE '2026-12-03', 'PoolC Project Demo Seminar', '00000000-0000-0000-0000-000000000003'),
  (23, true, 10, '매주 화 20:00-22:00', 'Rust 문법과 시스템 프로그래밍 기초를 다루는 스터디입니다.', 10, false, DATE '2026-12-10', 'Rust Systems Study', '00000000-0000-0000-0000-000000000004');

INSERT INTO activity_members (activity_id, member_login_id) VALUES
  (1, 'member1'),
  (1, 'member2'),
  (2, 'member2'),
  (2, 'member3'),
  (4, 'member1'),
  (4, 'member3'),
  (5, 'member2'),
  (7, 'member1'),
  (7, 'member2'),
  (8, 'member3'),
  (9, 'member1'),
  (9, 'member2'),
  (9, 'member3'),
  (10, 'member1'),
  (10, 'member2'),
  (11, 'member3'),
  (12, 'member1'),
  (12, 'member2'),
  (12, 'member3'),
  (13, 'member1'),
  (14, 'member2'),
  (15, 'member3'),
  (16, 'member1'),
  (16, 'member2'),
  (17, 'member3'),
  (18, 'member1'),
  (18, 'member2'),
  (19, 'member3'),
  (20, 'member1'),
  (20, 'member2'),
  (20, 'member3'),
  (21, 'member1'),
  (22, 'member2'),
  (22, 'member3'),
  (23, 'member1');

INSERT INTO activity_tag (id, activity_id, content) VALUES
  (1, 1, 'frontend'),
  (2, 1, 'react'),
  (3, 2, 'backend'),
  (4, 2, 'spring'),
  (5, 3, 'pks'),
  (6, 4, 'typescript'),
  (7, 4, 'frontend'),
  (8, 5, 'algorithm'),
  (9, 5, 'boj'),
  (10, 6, 'design-system'),
  (11, 6, 'ui'),
  (12, 7, 'spring'),
  (13, 7, 'backend'),
  (14, 8, 'unity'),
  (15, 8, 'gamejam'),
  (16, 9, 'devops'),
  (17, 9, 'kubernetes'),
  (18, 10, 'python'),
  (19, 10, 'automation'),
  (20, 11, 'nextjs'),
  (21, 11, 'ui'),
  (22, 12, 'cpp'),
  (23, 12, 'algorithm'),
  (24, 13, 'git'),
  (25, 13, 'collaboration'),
  (26, 14, 'os'),
  (27, 14, 'cs'),
  (28, 15, 'figma'),
  (29, 15, 'css'),
  (30, 16, 'database'),
  (31, 16, 'sql'),
  (32, 17, 'deploy'),
  (33, 17, 'web'),
  (34, 18, 'network'),
  (35, 18, 'http'),
  (36, 19, 'gamejam'),
  (37, 19, 'prototype'),
  (38, 20, 'security'),
  (39, 20, 'spring'),
  (40, 21, 'clean-code'),
  (41, 21, 'review'),
  (42, 22, 'demo'),
  (43, 22, 'project'),
  (44, 23, 'rust'),
  (45, 23, 'systems');

INSERT INTO session (id, activity_id, date, description, hour, session_number) VALUES
  (1, 1, DATE '2026-09-03', '개발 환경 구성과 코드베이스 읽기', 2, 1),
  (2, 1, DATE '2026-09-10', '게시판/세미나 UI 리팩토링', 2, 2),
  (3, 2, DATE '2026-09-05', '도메인 모델과 권한 구조 읽기', 2, 1),
  (4, 2, DATE '2026-09-12', 'Forms API 설계', 2, 2),
  (5, 4, DATE '2026-03-10', '타입 시스템과 컴포넌트 props', 2, 1),
  (6, 4, DATE '2026-03-17', '폼과 API 응답 타입 정리', 2, 2),
  (7, 5, DATE '2026-05-13', '그래프 탐색 문제 풀이', 2, 1),
  (8, 6, DATE '2026-07-04', '공통 카드와 페이지 레이아웃 정리', 2, 1),
  (9, 7, DATE '2025-09-09', 'Spring MVC와 계층 구조', 2, 1),
  (10, 8, DATE '2025-10-17', 'Unity 씬 구성과 입력 처리', 2, 1),
  (11, 9, DATE '2026-01-11', 'Docker 이미지와 배포 파이프라인', 3, 1),
  (12, 10, DATE '2026-09-08', 'Python 스크립트와 파일 처리', 2, 1),
  (13, 11, DATE '2026-09-15', 'Next.js 라우팅과 레이아웃', 2, 1),
  (14, 12, DATE '2026-09-22', 'C++ STL과 정렬 문제', 2, 1),
  (15, 13, DATE '2026-10-01', '브랜치 전략과 PR 리뷰', 2, 1),
  (16, 14, DATE '2026-10-08', '프로세스와 스레드', 2, 1),
  (17, 15, DATE '2026-10-15', '컴포넌트 시안 리뷰', 2, 1),
  (18, 16, DATE '2026-10-22', 'SELECT와 JOIN 연습', 2, 1),
  (19, 17, DATE '2026-10-29', '정적 사이트 배포 실습', 2, 1),
  (20, 18, DATE '2026-11-05', 'HTTP 요청 흐름 읽기', 2, 1),
  (21, 19, DATE '2026-11-12', '게임잼 아이디어 발산', 3, 1),
  (22, 20, DATE '2026-11-19', '인증과 세션 구조', 2, 1),
  (23, 21, DATE '2026-11-26', '리팩토링 예제 리뷰', 2, 1),
  (24, 22, DATE '2026-12-03', '프로젝트 데모 발표', 3, 1),
  (25, 23, DATE '2026-12-10', 'Rust 소유권과 빌림', 2, 1);

INSERT INTO project (id, created_at, updated_at, body, description, duration, genre, name, thumbnail_url)
VALUES
  (1, now() - interval '1 days', now() - interval '1 days', '**<게임잼 대상 수상작>**

[PoolC × KING 합동 게임잼 maKING POOL] 그놈들 팀

랜덤으로 얻는 마법의 순서를 설정하여 적을 막아내는 디펜스 게임

[다운로드](https://drive.google.com/drive/folders/1vdF1vkQvpFY984N8_X6WTrYCtJXsiDb1?usp=sharing)', '랜덤으로 얻는 마법의 순서를 설정하여 적을 막아내는 디펜스 게임.', '2026.07.03 ~ 2026.07.05', '로그라이크 슈팅 디펜스', '[maKING POOL] 운빨망법사', 'https://picsum.photos/seed/poolc-project-real-518/480/270'),
  (2, now() - interval '2 days', now() - interval '2 days', '**<게임잼 최우수상 수상작>**

[PoolC × KING 합동 게임잼 maKING POOL] 육각형 팀

''Face the randomness''는 랜덤 생성되는 맵에서 각종 무기를 이용해 계속해서 가혹해지는 세계를 탈출하는 익스트랙션 슈터 게임입니다.

외부 인원: KING 이우빈

[다운로드](https://drive.google.com/file/d/1Almu7pKZAc6GIdol4x6A6XEZUOp9m5Uk/view?usp=drive_link)', '''Face the randomness''는 랜덤 생성되는 맵에서 각종 무기를 이용해 계속해서 가혹해지는 세계를 탈출하는 익스트랙션 슈터 게임입니다.', '2026.07.03 ~ 2026.07.05', '2D 익스트랙션 슈터', '[maKING POOL] Face the randomness', 'https://picsum.photos/seed/poolc-project-real-517/480/270'),
  (3, now() - interval '3 days', now() - interval '3 days', '**<게임잼 우수상 수상작>**

[PoolC × KING 합동 게임잼 maKING POOL] 오버워치 팀

재앙도, 행운도 랜덤! 운과 순발력으로 살아남는 요리 게임

외부 인원: KING 황혜원, 전유진, 이영현, 문나린

[다운로드](https://github.com/joonsooan/kitchen-chaos)', '재앙도, 행운도 랜덤! 운과 순발력으로 살아남는 요리 게임', '2026.07.03 ~ 2026.07.05', '2D 캐주얼 요리 게임', '[maKING POOL] 냠냠 다이닝', 'https://picsum.photos/seed/poolc-project-real-516/480/270'),
  (4, now() - interval '4 days', now() - interval '4 days', '[PoolC × KING 합동 게임잼 maKING POOL] 포아성 팀

불행한 인생을 살아가는 소녀를 지켜주는 성좌 시뮬레이션 게임

외부 인원: KING 송은해, 장예주, 김지선, 장은서

[다운로드](https://github.com/boxboy523/Post-Apocalypse-Constellation/releases/tag/Release)', '불행한 인생을 살아가는 소녀를 지켜주는 성좌 시뮬레이션 게임', '2026.07.03 ~ 2026.07.05', '2D 간접조작 서바이벌', '[maKING POOL] 포스트 아포칼립스의 성좌가 되었다.', 'https://picsum.photos/seed/poolc-project-real-515/480/270'),
  (5, now() - interval '5 days', now() - interval '5 days', '[PoolC × KING 합동 게임잼 maKING POOL] Team. 솔로

같은 선택도 매번 다른 결과를 만드는 운빨 연애 시뮬레이션.
호감이면 쉬워지고, 비호감이면 더욱 험난해지는 미니게임을 돌파해 3일 안에 공략에 성공하세요.

외부 인원: KING 전민하, 심소은, 김나은, 윤민주

[다운로드](https://github.com/maKINGPool-Solo/Romance_Mangame)', '같은 선택도 매번 다른 결과를 만드는 운빨 연애 시뮬레��션.', '2026.07.03 ~ 2026.07.05', '연애 시뮬레이션, 비주얼 노벨', '[maKING POOL] 연애는 망겜이다.', 'https://picsum.photos/seed/poolc-project-real-514/480/270'),
  (6, now() - interval '6 days', now() - interval '6 days', 'The Final Jam은 동료들과 함께 음악을 연주하며 매 순간 다른 선택을 할 수 있는 게임입니다. 각 캐릭터들은 한 밴드의 구성원이고, 플레이어는 하나의 캐릭터를 맡아 리듬 게임을 플레이합니다. 플레이어는 정해져 있는 구조의 노래 안에서 어떤 음악을 연주할지 실시간으로 선택할 수 있습니다. 그리고 선택에 따른 채보와 음악으로 4키 리듬 게임을 플레이합니다.

참여 프로그램:
UNICON 루키상 수상, 베터그라운드 3차 밋업 올해의 게임 수상, NDM

드라이브 링크:
https://drive.google.com/file/d/1AzqTnlXSG5vW\_Vr3PlO8-hERrpzk9Mru/view?usp=sharing', 'The Final Jam은 동료들과 함께 음악을 연주하며 매 순간 다른 선택을 할 수 있는 게임입니다.', '2025.04 ~ 2026.02', '리듬 게임', 'The Final Jam', 'https://picsum.photos/seed/poolc-project-real-513/480/270'),
  (7, now() - interval '7 days', now() - interval '7 days', '뉴턴 역학이 지배하는 우주. 아폴로 시대의 비좁은 우주선에 갇혀, 열과 전력, 궤도를 통제하며 살아남으십시오. 극한의 하드 SF 로그라이크, Pilot 6174: 궤도 생존기입니다.

참여 프로그램:
2025 연세대학교 학생 창업자 조사, 2026 연고DEV

참여 인원:
배하람 (대표), 배준하 (외부 인원, 연세대학교 UIC QRM)

드라이브 링크:
https://drive.google.com/file/d/13S\_yhQr0MQvjfGjW5Y5lzIDmMG08XJAZ/view?usp=drive\_link', '뉴턴 역학이 지배하는 우주. 아폴로 시대의 비좁은 우주선에 갇혀, 열과 전력, 궤도를 통제하며 살아남으십시오. 극한의 하드 SF 로그라이크, Pilot 6174: 궤도 생존기입니다.', '2025.08.21~', '하드 SF, 시뮬레이션, 로그라이크', 'Pilot 6174: Orbital Survival', 'https://picsum.photos/seed/poolc-project-real-512/480/270'),
  (8, now() - interval '8 days', now() - interval '8 days', 'https://my-gemini-app-1.onrender.com/', 'AI 영어회화 웹', '2025.10.30~', '웹', 'AI 영어회화', 'https://picsum.photos/seed/poolc-project-real-511/480/270'),
  (9, now() - interval '9 days', now() - interval '9 days', '판타지 세계의 모험가 길드 접수원이 되어 돈을 버는 게임

참여 프로그램:
연고DEV

드라이브 링크:
https://drive.google.com/file/d/15DVQG-k00ua3b-NGFQBLGuZekRPdyZMr/view?usp=drive\_link', '판타지 ��계의 모험가 길드 접수원이 되어 돈을 버는 게임', '2025.10.10~', '경영, 도박', '담쟁이 이야기', 'https://picsum.photos/seed/poolc-project-real-510/480/270'),
  (10, now() - interval '10 days', now() - interval '10 days', '사막화 스팀펑크 세계관 속에서 펼쳐지는, 어린 아가씨를 지키기 위한 집사형 오토마톤의 하드보일드 액션 분투기

참여 프로그램:
NDM 2025 우수상 (재미 부문), 고연전 GameDev 2025 최우수상

드라이브 링크:
https://drive.google.com/drive/folders/1ErO3NNJnLbp-1JCWBeU0Obtk5gKUF-Ll', '사막화 스팀펑크 세계관 속에서 펼쳐지는, 어린 아가씨를 지키기 위한 집사형 오토마톤의 하드보일드 액션 분투기', '2024.05.07~', '액션, 스토리', 'MODULE:BERSERK', 'https://picsum.photos/seed/poolc-project-real-509/480/270'),
  (11, now() - interval '11 days', now() - interval '11 days', '미지의 행성에서 자원을 채굴, 가공하며 기지를 건설하고, 행성을 탈출하는 시뮬레이션 익스트랙션 게임

참여 프로그램:
2026 NDM 장려상, 연고전 GAMEDEV 2026 우수상

드라이브 링크:
https://drive.google.com/file/d/1kc2h7BYjO3VMoJ-8Ul0KFRw5wKXkJqFR/view?usp=sharing', 'Galaxy Scavengers는 미지의 행성에서 자원을 채굴, 가공하며 기지를 건설하고, 쏟아지는 적들을 뚫고 탈출하는 시뮬레이션 익스트랙션 장르 게임입니다.', '2025.08.14~', '시뮬레이션 익스트랙션', 'Galaxy Scavengers', 'https://picsum.photos/seed/poolc-project-real-508/480/270'),
  (12, now() - interval '12 days', now() - interval '12 days', '충돌로 카드를 연계하세요.
Reach The Orbit은 덱빌딩 로그라이크와 자동으로 발동되는 카드를 접목시킨 게임입니다.
무한에 가까운 콤보에 도달하기 위해 덱을 최적화하세요.
그리고 별들이 만들어내는 예측 불허의 움직임을 감상하세요.

참여 프로그램:
NDM 우수상

드라이브 링크:
https://drive.google.com/file/d/14FyiVOdEOL4vKQjALHSoeSJdX1IlfsL3/view?usp=sharing', 'Reach The Orbit은 덱빌딩 로그라이크와 자동으로 발동되는 카드를 접목시킨 게임입니다.', '2025.10.27~', '덱빌딩, 로그라이크, 당구', 'Reach The Orbit', 'https://picsum.photos/seed/poolc-project-real-507/480/270'),
  (13, now() - interval '13 days', now() - interval '13 days', '던전을 탐험하며 신들의 비밀에 대해 알아가는 1인칭 핵앤슬래시 로그라이트 게임 "Slayer of the Abyss"입니다. 몬스터를 베어내는 호쾌한 핵앤슬래시 액션, 몰려드는 적들을 거침 없이 썰어버리며 전장의 중심에 서보세요. 몬스터뿐 아니라 함정, 보물상자, 수상한 NPC까지, 매번 구조가 뒤틀리는 던전 속에서 예측 불가능한 모험을 경험하세요.

참여 프로그램:
NDM, 연고DEV

드라이브 링크:
https://drive.google.com/file/d/1QtvXu95gCPpRWID1L8vcBFOReesub7wV/view', '던전을 탐험하며 신들의 비밀에 대해 알아가는 1인칭 핵앤슬래시 로그라이트 게임 "Slayer of the Abyss"입니다.', '2025.12.25~', '3D 1인칭 핵앤슬래시 로그라이트', 'Slayer of the Abyss', 'https://picsum.photos/seed/poolc-project-real-506/480/270'),
  (14, now() - interval '14 days', now() - interval '14 days', '**<게임잼 대상 수상작>**

[제 2회 PoolC 게임잼 PoolCore] 도키도키 팀

창(Window)을 조작해 마음 속 비상상황을 뚫고 탈출하는, 감정 몰입형 2D 퍼즐 미연시 어드벤처. ――그날, 내 마음에… 불이 붙었다. 윈도우 창을 조작해 감정을 탈출하고, 타오르는 심장으로 선택지를 넘긴다! 이 비상(非常)상태, 고백으로 클리어할 수 있을까!?

[다운로드](https://drive.google.com/file/d/1Bz3zJ-I0FRudkL5SnnEhblD18b5xeBUt/view?usp=drive_link)', '창(Window)을 조작해 마음 속 비상상황을 뚫고 탈출하는, ��정 몰입형 2D 퍼즐 미연시 어드벤처', '2025.06.27 ~ 2025.06.29', '2D, 퍼즐, 미연시, 어드벤쳐', '[PoolCore] 내 마음속 B상', 'https://picsum.photos/seed/poolc-project-real-505/480/270'),
  (15, now() - interval '15 days', now() - interval '15 days', '**<게임잼 최우수상 수상작>**

[제 2회 PoolC 게임잼 PoolCore] 영웅호걸 팀

스트레스를 받으면 광대로 변하는 세계, 일확천금의 땅인 화성으로 가기 위해 누구보다 먼저 산을 올라야 하는 뱀주사위게임. 주사위를 굴려 뱀을 피하고, 사다리를 타고, 상대를 공격하세요. 필요하다면 광대가 되세요! 인생은 한 방입니다.

[다운로드](https://drive.google.com/file/d/1LPn3pCj2b2iYePzBjdUiycEGoSSzYVM2/view?usp=drive_link)', '일확천금의 땅인 화성으로 가기 위해 누구보다 먼저 산을 올라야 하는 뱀주사위게임', '2025.06.27 ~ 2025.06.29', '턴제, 주사위', '[PoolCore] To the Mars', 'https://picsum.photos/seed/poolc-project-real-504/480/270'),
  (16, now() - interval '16 days', now() - interval '16 days', '**<게임잼 우수상 수상작>**

[제 2회 PoolC 게임잼 PoolCore] INTP팀

화산 깊은 곳에서 자원을 캐던 채광 로봇 ''삐삐봇''이 갑작스러운 폭발로 작업장이 붕괴되어 탈출���는 내용의 게임으로 화면 아래에서 올라오는 용암을 피해 블록으로 길을 만들어 시간 내에 목표 지점까지 올라가는 플랫포머 게임입니다.

[다운로드](https://drive.google.com/file/d/1lnmKZwqwMStTtRkKYKk1RySz04qjLaoY/view?usp=drive_link)', '화산 깊은 곳에서 자원을 캐던 채광 로봇 ''삐삐봇''이 갑작스러운 폭발로 작업장이 붕괴되어 탈출하는 내용의 게임', '2025.06.27 ~ 2025.06.29', '퍼즐 플랫포머', '[PoolCore] 오늘도 화산입니다만 삐삐봇은 무사합니다', 'https://picsum.photos/seed/poolc-project-real-503/480/270'),
  (17, now() - interval '17 days', now() - interval '17 days', '[제 2회 PoolC 게임잼 PoolCore] 싱크홀 팀

공업용 수직터널을 파던 우리의 주인공. 천재지변으로 배수관이 터지고, 설상가상으로 터널도 무너지기 시작했다. 침착하게 제트팩을 타고 날아오르며, 굴착한 자원과 머리속의 설계도로 다양한 도구를 만들어, 지상으로 비상해라!

WASD로 움직이고, 좌클릭으로 레이저를 발사하세요.
1\~5 번호키를 꾹 눌러 수류탄을 만들고, 우클릭을 한 상태로 번호키를 눌러 해당 수류탄을 던지세요.
다양한 장애물이 떨어지는데, 그 중에는 계속해서 날기에 필수인 휘발유부터 철근, 콘크리트 같은 무속성 자원을 얻을 수 있는 장애물, 화약, 냉각수, 회로, 코일의 속성 자원을 얻을 수 있는 장애물이 있습니다. 이들을 레이저로 쏴 자원을 얻고, 지속해서 날아오르세요.
속성 장애물에 같은 속성의 수류탄을 던지면 속성 지대가 생성되며, 반대 속성(화약 <-> 냉각수, 회로 <-> 코일)의 수류탄을 던지면 즉시 소멸합니다.
5분간 비상하면, 탈출 완료입니다!

아트 외부 인원: 경영학과 이준표

[다운로드](https://drive.google.com/file/d/1SMrVG-QcTkPuqTWZLAhJV0mYJXybE55_/view?usp=drive_link)', '굴착한 자원과 머리속의 설계도로 다양한 도구를 만들어, 지상으로 비상해라!', '2025.06.27 ~ 2025.06.29', '액션', '[PoolCore] 비상:버티컬', 'https://picsum.photos/seed/poolc-project-real-502/480/270'),
  (18, now() - interval '18 days', now() - interval '18 days', '[제 2회 PoolC 게임잼 PoolCore] 도박쟁이 팀

16 Days는 16턴 동안 도시 자원(식량·에너지·데이터·의료)을 생산하고, 자원을 증폭·소비하며 비상재난사태를 무사히 넘기는 게임입니다.

[다운로드](https://drive.google.com/file/d/1QAIzxCupV3VHobnDWh_ipulwp-MaPvSH/view?usp=drive_link)', '도시 자원을 생산하고, 자원을 증폭·소비하며 비상재난사태를 무사히 넘기는 게임', '2025.06.27 ~ 2025.06.29', '엔진 빌딩, 전략', '[PoolCore] 16 Days', 'https://picsum.photos/seed/poolc-project-real-501/480/270'),
  (19, now() - interval '19 days', now() - interval '19 days', '방문은 실내를 연결합니다.
정원문은 야외를 연결합니다.
현관문은 실내와 야외를 연결합니다.
문의 규칙에 맞춰 방들을 연결해 보세요.

2025.02 빌드본 [\[다운로드\]](https://drive.google.com/file/d/1H_dne9P0jYTAUnpP1MOi6vHQwl0bM7-w/view?usp=sharing)', '문의 규칙에 맞춰 방들을 연결해 보세요.', '2024.08 ~', '캐주얼 모바일 퍼즐', 'Door Puzzle', 'https://picsum.photos/seed/poolc-project-real-453/480/270'),
  (20, now() - interval '20 days', now() - interval '20 days', '저주받은 성역에서 몰려오는 야수들을 정화하던 고귀한 기사와, 잿빛 도시에서 매일 밤 뒷세계의 범죄자들을 심판하던 하드보일드 형사의 세계가 충돌했습니다. 둘은 이제 힘을 합쳐 두 세계의 빛깔 속에 모습을 감춘 적들을 처치하며 영원한 전투를 이어가야 합니다.

* ''''Z''키를 눌러 아래 세계의 빛깔을 반전합니다. 폭력배는 빛깔이 드러나면 형사가 스스로 처치할 수 있게 됩니다.
* ''/''키를 눌러 위 세계의 빛깔을 반전합니다. 야수는 빛깔이 드러나면 기사가 스스로 처치할 수 있게 됩니다.
* 늑대인간은 빛깔을 감추지 않습니다. ''X''키를 눌러 은탄을 맞춰 쓰러트리세요.
* 흡혈귀는 빛깔을 감추지 않습니다. ''.''키를 눌러 성스러운 화살을 맞춰 쓰러트리세요.
* 폐유 드럼통을 저격하면 폭발하여 피해를 입힙니다. 은탄을 쏘지 말고 지나치세요.
* 판도라의 상자를 화살로 파괴하면 봉인된 악의가 해방됩니다. 화살을 쏘지 말고 지���치세요.

최대한 많은 적을 처치하여 최고점수를 기록하세요!

2025.02 빌드본 [\[플레이\]](https://iosif2510.itch.io/deux-noirs)', 'Deux Noirs(뒤 누아르)는 충돌한 두 세계의 영웅들이 세계의 빛깔을 반전하면서 빛깔 속에 숨은 적들을 끊임없이 처치하며 나아가는 하이퍼 캐주얼 게임입니다.', '2024.08 ~ 2025.02', '아케이드', 'DeuxNoirs', 'https://picsum.photos/seed/poolc-project-real-452/480/270'),
  (21, now() - interval '21 days', now() - interval '21 days', '체스와 로그라이크를 섞어서 체스의 시시각각 변화하는 전황과 로그라이크의 랜덤성을 융합하여 매판 새로운 전략을 세우고 캐릭터를 강화시키며 최종적으로 마왕을 처치하는 게임입니다.

2025.02 빌드본 [\[다운로드\]](https://drive.google.com/file/d/1lk9zrz-mozTiNsFEr4b_ol4K2gu3-SDJ/view?usp=drive_link) (미완성)', '체스와 로그라이크를 섞어서 체스의 시시각각 변화하는 전황과 로그라이크의 랜덤성을 융합한 전략 게임', '2024.09 ~', '추상전략 로그라이크', 'PieceHero', 'https://picsum.photos/seed/poolc-project-real-451/480/270'),
  (22, now() - interval '22 days', now() - interval '22 days', '[https://arxiv.org/abs/2410.18652](https://arxiv.org/abs/2410.18652)
[https://chartsquared.github.io/](https://chartsquared.github.io/)', 'AI 연구', '2024~', 'AI 연구/논문', 'C^2', 'https://picsum.photos/seed/poolc-project-real-401/480/270'),
  (23, now() - interval '23 days', now() - interval '23 days', '##### [https://arxiv.org/abs/2410.15876](https://arxiv.org/abs/2410.15876)

##### [https://flickerfusion305.github.io/](https://flickerfusion305.github.io/)', '#AI #RL #MARL', '2024.05.07~', 'AI 연구/논문', 'FlickerFusion', 'https://picsum.photos/seed/poolc-project-real-351/480/270'),
  (24, now() - interval '24 days', now() - interval '24 days', '[제 1회 PoolC 게임잼 PoolC.GG] 8팀 묵찌빠

묵찌빠의 신과 함께 더 흥미진진하고 다이나믹한 묵찌빠를 즐겨보세요!

아트 외부 인원: 통합디자인과 이유진

[다운로드](https://drive.google.com/file/d/19GmgXM_YMf3UXMtKQqzWj271ZvRkgz3m/view?usp=drive_link)', '묵찌빠의 신과 함께 더 흥미진진하고 다이나믹한 묵찌빠를 즐겨보세요!', '2024.06.28 ~ 2024.06.30', '턴제 전략 게임', '[PoolC.GG] 묵찌빠의 신', 'https://picsum.photos/seed/poolc-project-real-308/480/270'),
  (25, now() - interval '25 days', now() - interval '25 days', '[제 1회 PoolC 게임잼 PoolC.GG] 7팀 OMG

3가지 종교가 더 많은 신도를 차지하기 위해 경쟁하는 액션 게임

아트 외부 인원: 건설환경공학과 손은지

OP.GG 데스크탑 앱 등록 예정', '3가지 종교가 더 많은 신도를 차지하기 위해 경쟁하는 액션 게임', '2024.06.28 ~', '2D 액션, 캐주얼', '[PoolC.GG] 숭배 101', 'https://picsum.photos/seed/poolc-project-real-307/480/270'),
  (26, now() - interval '26 days', now() - interval '26 days', '**<게임잼 3위 작품>**

[제 1회 PoolC 게임잼 PoolC.GG] 6팀 신이 너무해

신앙을 생산하는 신도와 버프를 주는 타워를 적절히 배치하여 목표 신앙 포인트를 모으는 엔진빌딩 게임입니다.

아트 외부 인원: 통합디자인과 김아민

[다운로드](https://drive.google.com/drive/folders/1AunUWbR1pV_B9A7e_EvUwjiYldtHKWvm?usp=drive_link)', '신앙을 생산하는 신도와 버프를 주는 타워를 적절히 배치하여 목표 신앙 포인트를 모으는 엔진빌딩 게임입니다.', '2024.06.28 ~ 2024.06.30', '덱빌딩, 전략, 로그라이크', '[PoolC.GG] Zealementals', 'https://picsum.photos/seed/poolc-project-real-306/480/270'),
  (27, now() - interval '27 days', now() - interval '27 days', '**<게임잼 2위 작품>**

[제 1회 PoolC 게임잼 PoolC.GG] 5팀 송명근팀

목숨을 건 숭배자들의 전쟁 : 캐주얼 전략 퍼즐게임

아트 외부 인원: 정보인터랙션디자인과 류시원

[다운로드](https://drive.google.com/drive/folders/1v9p0DuWzDYcGRS6i469z3pj2Kd8Ld7X-?usp=drive_link)

<br>', '목숨을 건 숭배자들의 전쟁 : 캐주얼 전략 퍼즐게임', '2024.06.28 ~ 2024.06.30', '캐주얼 전략 퍼즐', '[PoolC.GG] Onelemental', 'https://picsum.photos/seed/poolc-project-real-305/480/270'),
  (28, now() - interval '28 days', now() - interval '28 days', '[제 1회 PoolC 게임잼 PoolC.GG] 4팀 UDK

점점 높아지는 경사지형에 둘러쌓인 동상을 끊임없이 굴러오는 돌덩이로부터 지켜야하는 1인칭 타워디펜스 게임입니다.

아트 외부 인원: 문화미디어과 양수진

OP.GG 데스크탑 앱 등록 예정

<br>', '점점 높아지는 경사지형에 둘러쌓인 동상을 끊임없이 굴러오는 돌덩이로부터 지켜야하는 1인칭 타워디펜스 게임입니다.', '2024.06.28 ~', '3D 타워 디펜스', '[PoolC.GG] UDK', 'https://picsum.photos/seed/poolc-project-real-304/480/270'),
  (29, now() - interval '29 days', now() - interval '29 days', '[제 1회 PoolC 게임잼 PoolC.GG] 3팀 로그

Dominus는 낯선 대륙에 착륙한 전사가 되어 영토를 정복하며 숭배자들을 모아 레벨업을 이루고, 마침내 마왕을 쓰러트리고 신이 되는 것을 목표로 하는, 다음 영토 타일 선택과 이동을 위한 대륙 화면과 2D 탑뷰 시점으로 이루어진 전투 스테이지들을 오가는 전략과 액션의 영역이 섞인 뱀서라이크 로그라이트 게임입니다.

아트 외부 인원: 통합디자인과 정지연

OP.GG 데스크탑 앱 등록 예정', 'Dominus는 낯선 대륙에 착륙한 전사가 되어 신이 되는 것을 목표로 하는 로그라이트 게임입니다.', '2024.06.28 ~', '액션, 전략, 로그라이트', '[PoolC.GG] Dominus', 'https://picsum.photos/seed/poolc-project-real-303/480/270'),
  (30, now() - interval '30 days', now() - interval '30 days', '**<게임잼 1위 작품>**

[제 1회 PoolC 게임잼 PoolC.GG] 2팀 6/45

6+1계명은 신이 되어 로또 당첨을 간절히 비는 신도에게 당첨 번호에 대한 계시를 내리는 카드 퍼즐 게임입니다.

[다운로드](https://drive.google.com/drive/folders/1CJM_gHMSp30Ah8EZpLKd31r0LvOMTxKC?usp=sharing)', '6+1계명은 신이 되어 로또 당첨을 간절히 비는 신도에게 당첨 번호에 대한 계시를 내리는 카드 퍼즐 게임입니다.', '2024.06.28 ~ 2024.06.30', '카드 게임', '[PoolC.GG] 6+1 계명', 'https://picsum.photos/seed/poolc-project-real-302/480/270'),
  (31, now() - interval '31 days', now() - interval '31 days', '[제 1회 PoolC 게임잼 PoolC.GG] 1팀 호감고닉

숭배 채팅으로 경기의 흐름을 바꿔라! ''숭배해야만 해''

<br>
[다운로드](https://drive.google.com/drive/folders/1VJ-f0UD9wNZS_bZG8B8FzVHsOpMILghI?usp=sharing)', '숭배 채팅으로 경기의 흐름을 바꿔라! ''숭배해야만 해''', '2024.06.28 ~ 2024.06.30', '오토배틀러, 시뮬레이션', '[PoolC.GG] 숭배해야만 해', 'https://picsum.photos/seed/poolc-project-real-301/480/270'),
  (32, now() - interval '32 days', now() - interval '32 days', '귀여운 카피바라 탑을 쌓아보세요!

\- 플랫폼: 윈도우\(Windows\)

\- 사용 엔진: 유니티

\- 1회 풀씨 게임잼 PoolC\.GG 운영진 제작

[다운로드](https://drive.google.com/drive/folders/1y1SIXrc1_M2Cwaoeae8Wu-YJTkqBuWQQ)', '귀여운 카피바라 탑을 쌓아보세요!', '2024.06.29 ~ 2024.06.30', '캐주얼', '카피바라 탑 쌓기', 'https://picsum.photos/seed/poolc-project-real-251/480/270'),
  (33, now() - interval '33 days', now() - interval '33 days', 'HellPunk는 매력적인 스토리, 짜릿한 액션과 유니크한 분위기를 선사하는 로그라이트 게임입니다.

신체를 개조하고, 무기와 스킬을 교체하고, 공격을 강화해 나아가며 탑을 정복하세요.

어느날 갑자기 사라진 가족��� 찾기 위해 전국을 돌아다니던 주인공, 애셔 드레이크.
그러던 어느날, 불의의 사고를 당하게 됩니다.

눈을 뜬 곳은 증기와 기계 돌아가는 소리가 가득한 지옥입니다.
본능적으로 눈 앞의 거대한 탑에 가족이 있음을 깨달은 주인공.

사라진 가족의 진실을 찾기 위해, 지옥의 탑을 끊임없이 오르내리며
죽음의 레이스를 시작합니다.

2024
\- 경기콘텐츠진흥원 지원사업 \(경기게임아카데미\) 선정
\- 제 2회 UNICON 우수상 수상
\- 제 1회 인천게임 페스티벌 참가
\- 제 3회 대구 콘텐츠 페어 참가

2025
\- Nexon 주관 게임제작발표회 \(NDM\) 대상 수상
\- 경기창업혁신센터\(구리\) 입주기업 선정
\- Neowiz 주관 고연전 GameDev 대상 수상
\- 인디 게임 개발 지원 \(개인사업자\) 선정
\- 한국인디게임협회 주관 인디오락실 선정
\- PlayX4 B2B\, B2C 참가
\- 2025 BIC 참가
\- 2025 도쿄게임쇼 인디게임부스 참가
\- 인디크래프트 일반부 선정

* **제 20회 경기게임오디션 최종 3위 수상**

HellPunk: Purgatorium 스팀 데모페이지 링크: https://store.steampowered.com/app/3784150/HellPunk\_Purgatorium\_Demo/', '고어 액션 로그라이트 게임 HellPunk', '2024.01.13 ~', '스피드, 고어, 액션, 로그라이트', 'HellPunk: Purgatorium', 'https://picsum.photos/seed/poolc-project-real-236/480/270'),
  (34, now() - interval '34 days', now() - interval '34 days', '(스토리)
박자에 따라서만 움직일 수 있는 저주의 이유를 찾기 위해
고군본투하는 용감한 강아지와 고양이의 이야기

(게임 소개)
Harmony’s Gambit은,
2인 협동게임이라는 게임 중추 시스템에 맞추어 기믹을 제작하였습니다.
일정 색 만이 시행할 수 있는 이벤트들을 다양화시켜 게임의 재미를 강화하고
정��성을 견고히 하고자 하였습니다.

(조작 방법)
1P: WASD
2P: 방향키
화면 상단부 노트를 맞춰야 움직일 수 있음

2024 NDM 출품

[다운로드](https://drive.google.com/drive/folders/1EeB-44PXgssirOf2M0BkH5zvjRPCcGbY?usp=sharing)', 'Harmony’s Gambit은, 모든 움직임을 박자(노트)에 따라서만 진행하며 미로형 맵을 탐험하고 스테이지를 클리어하는 2인용 협동게임입니다.', '2023.03.31 ~ 2024.02.20', '리듬, 액션, 어드벤쳐, 2인 협동', 'Harmony''s Gambit', 'https://picsum.photos/seed/poolc-project-real-235/480/270'),
  (35, now() - interval '35 days', now() - interval '35 days', '멸망해가는 행성의 구원을 위해 세상을 탐험하고, 막아서는 적을 무찌르는 주인공에 대한 이야기입니다. 소울라이크 3D 액션 어드벤처 게임이며, Unity를 사용해 제작하였습니다. 적을 공격해 무력화시키고, 이때 생긴 틈을 이용해 공격하며, 결과적으로는 무찌르는 것이 이 게임의 목표입니다.

조작법으로는, WASD를 이용해 이동, 여기에 Shift키를 더해 달리기, F키로 점프, Space키로 회피, Q키로 락온, E키로 아이템 사용을 진행합니다. 또한 마우스 왼쪽 클릭을 이용해 공격, 마우스 오른쪽 클릭을 이용해 방어와 패링을 수행합니다.

해당 작품은 NDM 2024에 출품되었습니다.

[다운로드 링크](https://drive.google.com/file/d/1OOkmNxunyFC4lGqla8pJQPfyqneUP5cd/view?usp=sharing)', '멸망해가는 행성의 구원을 위해 세상을 탐험하고, 막아서는 적을 무찌르는 주인공에 대한 이야기입니다.', '2023.10.05 ~', '액션 어드벤처', 'Azure Hope', 'https://picsum.photos/seed/poolc-project-real-234/480/270'),
  (36, now() - interval '36 days', now() - interval '36 days', '아무 기억 없이 쓰레기장에서 깨어난 주인공.

기억을 찾기 위해 그리고 잊어왔던 목적을 이루기 위해 모험을 떠난다

조작 방법 : 키보드 방향키 이동, Z공격, X상호작용

NDM 출품

[다운로드 링크](https://drive.google.com/file/d/1XdqQWynYskNCPSVFUdfRypy8FlyolrFq/view?usp=drive_link)', '한 망가진 로봇이 여러 모습으로 변신하며 기계 도시를 누비는 액션 어드벤처 2D 플랫포머 게임', '2023.10.31 ~', '2D 플랫포머, 액션, 어드벤쳐', 'Senti', 'https://picsum.photos/seed/poolc-project-real-233/480/270'),
  (37, now() - interval '37 days', now() - interval '37 days', '플레이어는 AI의 행동에 따라 얻을 수 있는 자원인 ''스택''을 소모하여 AI의 테트리스를 방해해야 합니다.

블럭을 배치하고 경험치를 얻어 강력한 특성들을 모아 더욱 효율적인 방해를 해보���요!

2024 NDM 최우수상 수상작

[다운로드 링크](https://drive.google.com/file/d/1kiy2-gPBusuzPDzHQhzmDhW53rSkYn6c/view?usp=sharing)', 'AI가 플레이하는 테트리스를 방해하는 퍼즐 게임', '2023.10.14 ~', '퍼즐', 'Sabotris', 'https://picsum.photos/seed/poolc-project-real-232/480/270'),
  (38, now() - interval '38 days', now() - interval '38 days', '공격을 피하고 반격하며 적을 쓰러트리는 손쉬운 경험을 제공합니다.

게임에 자신이 없는, 3D 멀미가 있는, 복잡한 시스템은 머리 아픈 분들도 즐길 수 있으면 좋겠다는 생각으로 제작하였습니다.

\*대화가 진행되지 않거나 벽이 뚫리는 등의 버그가 발생하면 F5를 눌러주세요\*

<br>
\- 공격\(Hit\) & 회피\(Run\) / 쉬운 조작\, 귀여운 그래픽\, 간단한 시스템

\- 플랫폼: 윈도우\(Windows\)

\- 사용 엔진: 유니티

\- 2024 NDM\(Nexon Dream Members\) 출품작

<br>
[다운로드 링크](https://drive.google.com/drive/folders/1o8yS92PGGwEELA_itfJp8w5gjoC2Z2Hg?usp=sharing)', '동물들을 구하고 흑막을 쓰러트려라!', '2023.11.02 ~ 2024.02.20', '캐주얼, 액션, 1:1 전투', 'Hit and Run', 'https://picsum.photos/seed/poolc-project-real-231/480/270'),
  (39, now() - interval '39 days', now() - interval '39 days', '**0\. 다운로드**
[다운로드](https://drive.google.com/drive/folders/1Ahqsgqw7x3ZhcQBGzi_9sJscvQsN9ASj?usp=sharing)
윈도우/mac용 파일 중 알맞은 파일을 다운받으세요.
잔버그들이 더러 있습니다... 추후 수정 예정이니 넓은 아량으로 이해해주세요!
멀티플레이는 아직 구현되지 않은 상태입니다.
재미있게 즐겨주세요!

**1\. 스토리**
도시에서 지친 일상을 벗어나 휴가를 즐기기 위해 수영장에 도착했습니다. 원래대로라면 평화로운 수영장에서 오리, 플라밍고 통통배를 타고 즐거운 시간을 보내야 하는데 문제가 생겼습니다. 갑자기 수영장에 진짜 배들이 나타나 다른 휴가객들과 통통배들을 위협하고 있습니다! 통통배를 조종해서 수영장에 침입한 배들을 격퇴해 주세요!

<span class="colour" style="color:rgb(0, 32, 96)">**2\. 게임 소개**</span>
오리, 공작이나 플라밍고 모양의 통통배를 조종하여 적 배를 침몰시키세요! 통통배에는 5명의 선원이 탑승하고 있으며, 선원을 뱃사공과 포수로 원하는 대로 분배할 수 있습니다. 뱃사공의 수가 많아지면 배의 속도가 빨라지고, 포수의 수를 늘리면 포의 장전 속��가 빨라집니다. 게임을 진행하면서 새로운 선원을 구조해 통통배의 능력을업그레이드하세요. 자신만의 전략으로 통통배를 지키고 적을 처치하세요!

**3\. 플레이 방법**
(1) 통통배 조종
플레이하게 되는 통통배는 wasd키로 조종할 수 있습니다. 단, 물 위인 만큼 배가 조종하기 어려울 수 있습니다!
(2) 폭탄 발사
우클릭으로 포를 배의 오른쪽에서 발사할 수 있습니다. 포는 발사된지 4초 후에 터지기 때문에 너무 멀리서 발사하면 안됩니다!
(3) 인원분배
UI 혹은 ''q''와 ''e'' 키로 뱃사공과 포수의 수를 조절할 수 있습니다. 사공이 많을 수록 배가 빨라지고 조종하기 쉬워집니다. 포수가 많을수록 장전이 빨라집니다.
(4) 적 통통배
적 배는 레벨이 올라갈수록 강력해지고 특수한 능력을 가지게 됩니다. 다양한 적 AI 배를 전략적으로 격파하세요!
(5) 추가 선원
일정 시간이 되면 통통배에 탑승하고자 하는 사람이 등장합니다. 근처에서 잠시 배를 멈추면 사람이 배에 타게 됩니다. 사람을 태워 선원의 수를 늘리세요!', '통통배로 적 배들을 물리치세요!', '2023.08.01 ~ 2023.09.01', '슈팅, 캐주얼', '다른 배들 통통', 'https://picsum.photos/seed/poolc-project-real-181/480/270'),
  (40, now() - interval '40 days', now() - interval '40 days', '땅을 파고 내려가며 광물을 모아 장비를 강화하고, 문지기 보스를 없애 새로운 도시에 진입하며 탐험하는 게임입니다. 수집과 성장의 재미를 제공하고 세계관을 즐길 수 있습니다.', '오리엔탈 스팀펑크 굴착 게임', '2023.01.13~2023.02.23', '굴착, 2D 플랫포머, 보스전투', '굴착협객', 'https://picsum.photos/seed/poolc-project-real-133/480/270'),
  (41, now() - interval '41 days', now() - interval '41 days', '<중요>
인터넷 연결을 필요로 하는 1:1 pvp 게임입니다. 혼자 게임에 접속해 있으실 경우 플레이할 수 없습니다. 친구를 상대로 게임을 플레이 해보세요!
[다운로드](https://drive.google.com/drive/folders/1eMxDhPaYq5hzLS3YlsjLwrRW9Co2rlDW?usp=sharing)
윈도우용 : MAZE_Survival
Apple Silicon용 : apple_MAZESurvival

1\. 스토리
고대 유적에 많은 보물이 묻혀있다는 소문을 들은 에밀리, 알렉스, 루슬라는 도굴꾼이 되어 유적에 들어가게 됩니다. 결국 유적에 들어와서 보물을 찾았다고 생각한 순간, 보물을 지키는 괴물이 나타났습니다. 우연히 자신 이외의 도굴꾼이 한명 더 유적에 있다는 것을 알게 되었는데, 이 도굴꾼을 괴물에게 재물로 바쳐 이 공간에서 탈출해야 합니다.

2\. 조작
WASD 키를 이용하여 방향을 조작합니다. 또한, 코인을 소모하여 기믹을 사용할 수 있습니다.
마우스 좌클릭을 이용하여 3코인으로 블록 생성, 2코인으로 블록 제거를 할 수 있습니다. Q를 누르면 8코인으로 괴물을 공포 상태로 만들어 도망가게 할 수 있습니다. 마지막으로, space를 눌러 캐릭터별 특수 기믹을 사용할 수 있습니다.

3\. 플레이
게임이 시작되면 괴물은 가까운 플레이어를 향해 걸어갑니다. 자신의 앞을 막는 블록을 제거하여 닫힌 공간에서 괴물의 추적으로부터 벗어나고, 상대방이 지나가는 길목에 블록을 만들어 상대방의 움직임을 제한하세요. 혹은 자신 캐릭터만의 특수 기믹을 사용하여 전략적으로 상대방이 괴물에게 잡히게 만드세요. (캐릭터는 랜덤으로 배정됩니다.)', '블록 생성, 제거를 이용해서 괴물이 상대를 먼저 잡아먹도록 하는 게임', '2022.12.01 - 2023.02.23', '액션, 전략, pvp', 'MAZE Survival', 'https://picsum.photos/seed/poolc-project-real-132/480/270'),
  (42, now() - interval '42 days', now() - interval '42 days', '포스트 아포칼립스 배경��� 서울에서 칵테일 바를 운영하는 이야기
2023 겨울 NDM 출품작
2023 UNICON 출품작
2023.02 빌드본 [다운로드](https://drive.google.com/file/d/12Jc_XxOYyvqNrlXJJv8NznB0uP436N-9/view?usp=share_link)
2023.08 빌드본 [다운로드](https://iosif2510.itch.io/ba)
2024년 겨울 NDM, IndieGo 출품작
2024.03 빌드본 [다운로드](https://drive.google.com/file/d/1EoX4NpqB_gwjj3Gyx8_GnCTR5w7qn0yc/view?usp=sharing)', '칵테일 바 시뮬레이션/타이쿤', '2023.01.21 ~', '시뮬레이션, 타이쿤', 'BA', 'https://picsum.photos/seed/poolc-project-real-131/480/270'),
  (43, now() - interval '43 days', now() - interval '43 days', '### 프로젝트 소개

![image]([image])

### 아키텍처
<br>
![image]([image]', '감정 일기 공유 서비스', '2021.08 ~ 2022.02', '웹', '이모티 Emotie', 'https://picsum.photos/seed/poolc-project-real-81/480/270'),
  (44, now() - interval '44 days', now() - interval '44 days', '2021년을 맞아 풀씨 홈페이지에 새로운 기능을 추가하여 리뉴얼

<br>
프론트엔드: 김민지
백엔드: 박형철&정윤석&양정일
배포 등 기타 도움: 양정일
<br>
##### 기술 스택

* 프론트엔드: JavaScript, React, Redux, Redux-saga
* 백엔드: Java, Spring, JPA
* DB: Postgresql
* 배포: docker, travis, aws, nginx

##### 추가된 기능

* 동아리 소개
* 멤버 목록 조회 및 멤버가 참여한 활동 조회
* 프로젝트 참여자 추가 기능
* 세미나/스터디 개설 및 신청, 출석 관리 기능
* 도서 조회 및 대출 관련 기능
* 최소활동기준 만족 여부 확인 기능(관리자 페이지)

##### 수정된 기능

* 게시물 작성 에디터를 toast-ui editor로 교체
* 모달창을 통해 파일 업로드 가능
* 페이지네이션', '풀씨 홈페이지 리뉴얼 프로젝트', '2021.01.16 ~', '웹', 'Haribo & Mincho', 'https://picsum.photos/seed/poolc-project-real-31/480/270'),
  (45, now() - interval '45 days', now() - interval '45 days', '플랫폼 : PC, 모바일

개요 : 총 5개의 스테이지로 이루어져 있으며 각각의 스테이지마다 정해진 패턴의 탄막을 피해 해골머리 보스를 쓰려뜨려야 하는 플랫포머 게임

비고 : itch.io MyFirstGameJam 참가 작

[Downloadable Link](https://unrealchan.itch.io/eternal-hero)', '2D 플랫포머 탄막 게임입니다.', '2021-01-09 ~ 2021-01-24', '플랫포머', 'Eternal Hero', 'https://picsum.photos/seed/poolc-project-real-22/480/270'),
  (46, now() - interval '46 days', now() - interval '46 days', '플랫폼 : PC with VR
개요 : 버려진 폐공장의 분위기를 토대로 퍼즐들을 풀어 탈출하는 방탈출 게임입니다.

비고 : 기존 구글 카드보드 게임기획 -> 스토리진행 VR 호러 게임 -> 액션 어드벤쳐 VR 호러 게임 -> VR 방탈출의 기획안 3단엎음이 있었음', 'VR을 이용한 방탈출 게임입니다.', '2020-05-20 ~', 'VR, Puzzle', 'VR Room Escape', 'https://picsum.photos/seed/poolc-project-real-19/480/270'),
  (47, now() - interval '47 days', now() - interval '47 days', '플랫폼 : 안드로이드
개요 : 칵테일 바에서 바텐더와 이야기를 나누는 감성을 느낄 수 있는 게임입니다.', '새 손님들의 고민을 들어주는 칵테일바 시뮬레이션 게임', '2020-05-20 ~', '감성, 휴먼, 스토리', 'Yourtail', 'https://picsum.photos/seed/poolc-project-real-18/480/270'),
  (48, now() - interval '48 days', now() - interval '48 days', '플랫폼 : 안드로이드
개요 : 간단하게 즐길 수 있는 디펜스 형식의 하이퍼 캐쥬얼 게임', '서지호님이 제작한 2D 하이퍼 캐쥬얼 게임입니다.', '2019-10-17 ~ 2020-08-31', '모바일 하이퍼 캐쥬얼', 'Star Planet', 'https://picsum.photos/seed/poolc-project-real-17/480/270'),
  (49, now() - interval '49 days', now() - interval '49 days', '플랫폼 : 안드로이드
개요 : 동물 캐릭터들의 스토리를 따라 카페를 운영하는 경영게임
비고 : 2022년 2월에 게임 출시 완료', '귀여운 동물캐릭터들의 카페 경영게임', '2019-10-17 ~ 2020-12-31', '경영', 'Cafe Forest', 'https://picsum.photos/seed/poolc-project-real-16/480/270'),
  (50, now() - interval '50 days', now() - interval '50 days', 'react.js, golang(graphql) 을 이용하여 홈페이지 제작', '풀씨 홈페이지 제작', '2018-10-23 ~ 2019-02-05', '웹', 'Nagase, SigAhri', 'https://picsum.photos/seed/poolc-project-real-15/480/270'),
  (51, now() - interval '51 days', now() - interval '51 days', '플랫폼 : PC
개요 : 리듬게임과 플랫포머 게임을 조합한 게임
비고 : NDM 출품작 [다운로드](https://drive.google.com/open?id=1pr4_QRpAdn7F_TurqO7NnAtoL0hpwO3E)', '2D 리듬, 플랫포머 게임', '2019-03-01 ~ 2019-08-23', '게임', 'Bootleg', 'https://picsum.photos/seed/poolc-project-real-14/480/270'),
  (52, now() - interval '52 days', now() - interval '52 days', '플랫폼 : PC
개요 : 플랫포머 기반의 와이어 액션을 추가한 환상적이고 평화로운 분위기의 게임
비고 : NDM 출품작 [다운로드](https://drive.google.com/open?id=1syN6D8aM8D5ssuvsfYry7K0ixXE6NtdD)', '2D 플랫포머 게임', '2019-03-01 ~ 2019-08-23', '게임', '산나비', 'https://picsum.photos/seed/poolc-project-real-13/480/270'),
  (53, now() - interval '53 days', now() - interval '53 days', '플랫폼 : PC
개요 : 빛의 삼원색 RGB의 특징을 게임으로 승화시켜 상대의 색의 보색에 대응되는 총알을 발사하여야만 적을 물리칠 수 있는 슈팅 생존 게임
비고 : NDM 출품작 [다운로드](https://drive.google.com/open?id=1O-2Ab4snuAb5dZQF4yhNtq-dPLeCxUUL)', '탑뷰 슈팅 생존 게임', '2019-03-01 ~ 2019-08-23', '게임', '보오색 슈터', 'https://picsum.photos/seed/poolc-project-real-12/480/270'),
  (54, now() - interval '54 days', now() - interval '54 days', '플랫폼 : PC
개요 : 적절한 카드를 적절한 타이밍에 적절한 타워를 향하여 공격 명령을 내려서 스테이지를 클리어해 나가는 전략 게임
비고 : NDM 출품작 [다운로드](https://drive.google.com/open?id=1ASHRahUd7LHdgGERtl9xsdz8slPROrJ1)', '카드를 이용한 전략 게임', '2019-03-01 ~ 2019-08-23', '게임', '쫄병이 아니야', 'https://picsum.photos/seed/poolc-project-real-11/480/270'),
  (55, now() - interval '55 days', now() - interval '55 days', '- 플랫폼: Android
- 개요: 2D 탄막 슈팅 게임. 기존 탄막 슈팅 게임의 형식에서 탈피, 적의 공격을 방패로 흡수하고 에너지로 충전하여 다시 반격하는 리플렉팅 시스템을 구현한 모바일 게임.
- 비고: 라인 게임 제작 동아리 활동 출품작', '2D 탄막 슈팅 게임', '2018-02-01 ~ 2018-08-31', '게임', 'Reflector', 'https://picsum.photos/seed/poolc-project-real-10/480/270'),
  (56, now() - interval '56 days', now() - interval '56 days', '- 플랫폼: PC
- 개요: 2D 탑다운 멀티플레이어 슈팅 게임. 네트워크 솔루션으로 Freetier PUN을 사용하여 최대 4인 참여 플레이 구현. 직관적인 조작과 다양한 아이템, 무기 등으로 다채로운 플레이 가능.
- 비고: 넥슨 게임 제작 동아리 활동 출품작', '2D 탑다운 멀티플레이어 슈팅 게임', '2018-01-01 ~ 2018-02-28', '게임', 'Hair Scramble', 'https://picsum.photos/seed/poolc-project-real-9/480/270'),
  (57, now() - interval '57 days', now() - interval '57 days', '- 플랫폼: PC, VR
- 개요: VR 비주얼 노벨 게임. 대화나 사용자 인풋에 따른 상호작용을 기반으로 하는 호감도 시스템도 구현.
- 플랫폼: 넥슨 게임 제작 동아리 활동 출품작', 'VR 비주얼 노벨 게임', '2017-01-01 ~ 2017-02-28', '게임', 'Project VR', 'https://picsum.photos/seed/poolc-project-real-8/480/270'),
  (58, now() - interval '58 days', now() - interval '58 days', '- 플랫폼: Android
- 개요: 모바일 캐주얼 클릭 게임. 시간 경과 전에 가장 아래에 있는 블록들을 클릭하여 제거하는 방식.
- 비고: 라인 게임 제작 동아리 활동 출품작', '모바일 캐주얼 클릭 게임', '2016-07-01 ~ 2016-08-31', '게임', 'Up', 'https://picsum.photos/seed/poolc-project-real-7/480/270'),
  (59, now() - interval '59 days', now() - interval '59 days', '- 플랫폼: PC
- 온라인 멀티 타워디펜스 게임.
플레이어가 공격과 수비측 모두를 일대다 형식으로 플레이하는 게임으로, 공격 시 사망 시에도 랜덤으로 공격측 캐릭터를 조작가능하고 수비측은 직접 타워를 조작하여 적을 저격하는 슈팅게임의 요소도 포함.
- 2016 동계 넥슨 게임 제작 동아리 활동 출품작', '온라인 멀티 타워디펜스 게임', '2016-01-01 ~ 2016-02-29', '게임', '우리는 무적의 중국인', 'https://picsum.photos/seed/poolc-project-real-6/480/270'),
  (60, now() - interval '60 days', now() - interval '60 days', '- 개요: 간단한 터치를 통한 우측, 하측 이동으로 장애물을 파괴하여 길을 찾는 퍼즐 게임
- 플랫폼: Android
- 라인 게임 제작 동아리 활동 출품작.
- Google Play 스토어 출시! ([다운 받으러 가기](https://play.google.com/store/apps/details?id=com.PoolC.Right_Down))', '안드로이드 퍼즐 게임', '2018-09-01 ~ 2018-12-30', '게임', 'RightDown', 'https://picsum.photos/seed/poolc-project-real-3/480/270'),
  (61, now() - interval '61 days', now() - interval '61 days', '- 개요 : 싱글 VR 게임/시뮬레이터. HTC VIVE를 이용하여 오버워치 게임 캐릭터인 겐지와 맥크리의 각종 공격 및 기술 구현.
- 플랫폼: PC, VR
- 2016년 넥슨 게임 제작 동아리 활동 출품작', '싱글 VR 게임 /시뮬레이터', '2016-07-01 ~ 2016-08-31', '게임', 'Genji & McCree VR', 'https://picsum.photos/seed/poolc-project-real-2/480/270');

INSERT INTO project_members (project_id, member_loginids) VALUES
  (1, 'admin'),
  (2, 'president'),
  (3, 'member1'),
  (4, 'member2'),
  (5, 'admin'),
  (6, 'president'),
  (7, 'member1'),
  (8, 'member2'),
  (9, 'admin'),
  (10, 'president'),
  (11, 'member1'),
  (12, 'member2'),
  (13, 'admin'),
  (14, 'president'),
  (15, 'member1'),
  (16, 'member2'),
  (17, 'admin'),
  (18, 'president'),
  (19, 'member1'),
  (20, 'member2'),
  (21, 'admin'),
  (22, 'president'),
  (23, 'member1'),
  (24, 'member2'),
  (25, 'admin'),
  (26, 'president'),
  (27, 'member1'),
  (28, 'member2'),
  (29, 'admin'),
  (30, 'president'),
  (31, 'member1'),
  (32, 'member2'),
  (33, 'admin'),
  (34, 'president'),
  (35, 'member1'),
  (36, 'member2'),
  (37, 'admin'),
  (38, 'president'),
  (39, 'member1'),
  (40, 'member2'),
  (41, 'admin'),
  (42, 'president'),
  (43, 'member1'),
  (44, 'member2'),
  (45, 'admin'),
  (46, 'president'),
  (47, 'member1'),
  (48, 'member2'),
  (49, 'admin'),
  (50, 'president'),
  (51, 'member1'),
  (52, 'member2'),
  (53, 'admin'),
  (54, 'president'),
  (55, 'member1'),
  (56, 'member2'),
  (57, 'admin'),
  (58, 'president'),
  (59, 'member1'),
  (60, 'member2'),
  (61, 'admin');

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
