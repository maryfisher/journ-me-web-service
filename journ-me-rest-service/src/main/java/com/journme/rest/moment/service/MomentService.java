package com.journme.rest.moment.service;

import com.journme.domain.MomentBase;
import com.journme.domain.MomentDetail;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.moment.repository.MomentBaseRepository;
import com.journme.rest.moment.repository.MomentDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 31.10.2015
 */
public class MomentService {

    @Autowired
    private MomentBaseRepository momentBaseRepository;

    @Autowired
    private MomentDetailRepository momentDetailRepository;

    public MomentBase save(MomentBase moment) {
        return momentBaseRepository.save(moment);
    }

    public MomentDetail save(MomentDetail moment) {
        return momentDetailRepository.save(moment);
    }

    public MomentDetail getMomentDetail(String momentId) {
        MomentDetail momentDetail = momentDetailRepository.findOne(momentId);
        if (momentDetail == null) {
            throwExc(momentId);
        }

        return momentDetail;
    }

    public MomentBase getMomentBase(String aliasId) {
        MomentBase moment = momentBaseRepository.findOne(aliasId);
        if (moment == null) {
            throwExc(aliasId);
        }

        return moment;
    }

    private void throwExc(String momentId) {
        throw new JournMeException("No Moment found for given ID " + momentId,
                Response.Status.BAD_REQUEST,
                JournMeExceptionDto.ExceptionCode.MOMENT_NONEXISTENT);
    }
}
