package com.journme.rest.moment.service;

import com.journme.domain.MomentBase;
import com.journme.domain.MomentDetail;
import com.journme.domain.QMomentDetail;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.contract.JournMeExceptionDto;
import com.journme.rest.moment.repository.MomentBaseRepository;
import com.journme.rest.moment.repository.MomentDetailRepository;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.DateTimePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

import javax.ws.rs.core.Response;
import java.util.Date;

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

    public long countAll() {
        return momentBaseRepository.count();
    }

    public Page<MomentDetail> getMomentsByDate(Date from, Date to) {
        DateTimePath<Date> qCreated = QMomentDetail.momentDetail.created;

        BooleanExpression criteria = QMomentDetail.momentDetail.isPublic.isTrue();
        if (from != null) {
            criteria.and(qCreated.after(from));
        }
        if (to != null) {
            criteria.and(qCreated.before(to));
        }

        return momentDetailRepository.findAll(criteria, new PageRequest(0, 10, Direction.DESC, qCreated.getMetadata().getName()));
    }

    private void throwExc(String momentId) {
        throw new JournMeException("No Moment found for given ID " + momentId,
                Response.Status.BAD_REQUEST,
                JournMeExceptionDto.ExceptionCode.MOMENT_NONEXISTENT);
    }
}
