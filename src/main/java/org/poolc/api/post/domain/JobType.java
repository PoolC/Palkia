package org.poolc.api.post.domain;

import lombok.Getter;

@Getter
public enum JobType {
    INTERN_FOR_EXPERIENCE {
        public String getDescription() { return "체험형 인턴"; }
    },
    INTERN_FOR_JOB {
        public String getDescription() { return "전환형 인턴"; }
    },
    NEW_EMPLOYEE {
        public String getDescription() { return "신입 채용"; }
    },
    EXPERIENCED_EMPLOYEE {
        public String getDescription() { return "경력직 채용"; }
    },
    BOOTCAMP {
        public String getDescription() { return "부트캠프"; }
    },
    COMPETITION {
        public String getDescription() { return "공모전"; }
    }
}
