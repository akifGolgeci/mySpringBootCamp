package com.example.user.mapper;

import com.example.user.dto.SiteRequestDto;
import com.example.user.dto.SiteResponseDto;
import com.example.user.entity.Site;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SiteMapper {
    SiteResponseDto siteToSiteResponse(Site site);
    List<SiteResponseDto> siteToSiteResponseList(List<Site> sites);

    Site siteRequestDtoToSite(SiteRequestDto siteRequestDto);
}
