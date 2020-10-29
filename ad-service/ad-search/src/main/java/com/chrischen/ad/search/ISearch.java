package com.chrischen.ad.search;

import com.chrischen.ad.search.vo.SearchRequest;
import com.chrischen.ad.search.vo.SearchResponse;

/**
 * Created by Chris Chen
 */
public interface ISearch {
    SearchResponse fetchAds(SearchRequest request);
}
