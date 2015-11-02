package com.journme.rest.alias.service;

import com.journme.domain.AliasBase;
import com.journme.domain.AliasDetail;
import com.journme.rest.alias.repository.AliasBaseRepository;
import com.journme.rest.alias.repository.AliasDetailRepository;
import com.journme.rest.common.errorhandling.JournMeException;
import com.journme.rest.contract.JournMeExceptionDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 31.10.2015
 */
public class AliasService {

    @Autowired
    private AliasDetailRepository aliasDetailRepository;

    @Autowired
    private AliasBaseRepository aliasBaseRepository;

    public AliasBase save(AliasBase alias) {
        return aliasBaseRepository.save(alias);
    }

    public AliasDetail save(AliasDetail alias) {
        return aliasDetailRepository.save(alias);
    }

    public AliasDetail getAliasDetail(String aliasId) {
        AliasDetail aliasDetail = aliasDetailRepository.findOne(aliasId);
        if (aliasDetail == null) {
            throwAliasExc(aliasId);
        }

        return aliasDetail;
    }

    public AliasBase getAliasBase(String aliasId) {
        AliasBase alias = aliasBaseRepository.findOne(aliasId);
        if (alias == null) {
            throwAliasExc(aliasId);
        }

        return alias;
    }

    private void throwAliasExc(String aliasId) {
        throw new JournMeException("No Alias found for given alias ID " + aliasId,
                Response.Status.BAD_REQUEST,
                JournMeExceptionDto.ExceptionCode.ALIAS_NONEXISTENT);
    }
}
