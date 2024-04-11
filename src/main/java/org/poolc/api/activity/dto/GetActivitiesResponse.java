package org.poolc.api.activity.dto;

import lombok.Getter;
import org.poolc.api.activity.domain.Activity;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetActivitiesResponse {
    private final Long id;
    private final String title;
    private final HostResponse host;
    private final LocalDate startDate;
    private final boolean available;
    private final List<TagResponse> tags;
    private final Long capacity;
    private final boolean isSeminar;
    private final Long hour;
    private final List<String> memberLoginIds;

    public GetActivitiesResponse(Long id, String title, HostResponse host, LocalDate startDate, boolean available, List<TagResponse> tags, Long capacity, boolean isSeminar, Long hour,List<String> memberLoginIds) {
        this.id = id;
        this.title = title;
        this.host = host;
        this.startDate = startDate;
        this.available = available;
        this.tags = tags;
        this.capacity = capacity;
        this.isSeminar = isSeminar;
        this.hour = hour;
        this.memberLoginIds = memberLoginIds;
}

    public static GetActivitiesResponse of(Activity activity) {
        List<TagResponse> tags = activity.getTags().stream()
                .map(t -> new TagResponse(t))
                .collect(Collectors.toList());
        HostResponse host = HostResponse.of(activity.getHost());
        return new GetActivitiesResponse(activity.getId(), activity.getTitle(), host,
                activity.getStartDate(), activity.getAvailable(),
                tags, activity.getCapacity(), activity.getIsSeminar(), activity.getHour(), activity.getMemberLoginIDs()
                );
    }
}
