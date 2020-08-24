package com.chrischen.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Chris Chen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeUnitResponse {

    private List<Long> ids;
}
