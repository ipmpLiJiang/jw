package com.welb.organization_check.controller;

import com.welb.organization_check.service.IScoreDutySmService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;

@RestController
@RequestMapping("/scoredutysm")
public class ScoreDutySmController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IScoreDutySmService scoreDutySmService;
}
