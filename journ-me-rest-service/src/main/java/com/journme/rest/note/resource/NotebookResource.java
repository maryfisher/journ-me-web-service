package com.journme.rest.note.resource;

import com.journme.domain.AliasDetail;
import com.journme.domain.JourneyDetail;
import com.journme.domain.Notebook;
import com.journme.domain.repository.NotebookRepository;
import com.journme.rest.alias.service.AliasService;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.journey.service.JourneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 07.03.2016
 */
@Component
@Singleton
public class NotebookResource extends AbstractResource {

    @Autowired
    private JourneyService journeyService;

    @Autowired
    private AliasService aliasService;

    @Autowired
    private NotebookRepository notebookRepository;

    @POST
    @ProtectedByAuthToken
    public Notebook createNotebook(
            @QueryParam("aliasId") String aliasId,
            @QueryParam("journeyId") String journeyId,
            @NotNull @Valid Notebook notebook) {

        notebook.setId(null); //ensures that new Notebook is created in the collection

        notebook = notebookRepository.save(notebook);

        if (aliasId != null) {
            AliasDetail aliasDetail = aliasService.getAliasDetail(aliasId);
            aliasDetail.getNotebooks().add(notebook);
            aliasService.save(aliasDetail);
        } else if (journeyId != null) {
            JourneyDetail journeyDetail = journeyService.getJourneyDetail(journeyId);
            journeyDetail.getNotebooks().add(notebook);
            journeyService.save(journeyDetail);
        }

        return notebook;
    }
}