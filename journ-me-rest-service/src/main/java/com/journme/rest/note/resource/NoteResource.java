package com.journme.rest.note.resource;

import com.journme.domain.Note;
import com.journme.domain.NoteImage;
import com.journme.domain.QNote;
import com.journme.domain.repository.NoteImageRepository;
import com.journme.domain.repository.NoteRepository;
import com.journme.rest.common.filter.ProtectedByAuthToken;
import com.journme.rest.common.resource.AbstractResource;
import com.journme.rest.common.searchfilter.NoteSearchFilter;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.types.Predicate;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.inject.Singleton;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;

/**
 * @author mary_fisher
 * @version 1.0
 * @since 18.02.2016
 */
@Component
@Singleton
public class NoteResource extends AbstractResource.AbstractImageResource<NoteImage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoteResource.class);

    @Autowired
    private NoteRepository noteRepository;

    private NoteImageRepository noteImageRepository;

    @Autowired
    public NoteResource(NoteImageRepository noteImageRepository) {
        super(noteImageRepository);
        this.noteImageRepository = noteImageRepository;
    }

    @POST
    @Path("/search")
    public Page<Note> searchNotes(@Min(0) @QueryParam("pageNumber") int pageNumber,
                                  @Min(1) @Max(100) @QueryParam("pageSize") int pageSize,
                                  @DefaultValue("DESC") @QueryParam("sortDirection") Sort.Direction sortDirection,
                                  @DefaultValue("created") @QueryParam("sortProperty") String sortProperty,
                                  NoteSearchFilter searchFilter) {
        LOGGER.info("Incoming call to search notes");

        return noteRepository.findAll(fromSearchFilter(searchFilter),
                new PageRequest(pageNumber, pageSize, sortDirection, sortProperty));
    }

    //TODO move to service?
    protected Predicate fromSearchFilter(NoteSearchFilter sf) {
        BooleanBuilder predicate = new BooleanBuilder();
        QNote qNote = QNote.note;

        if (sf != null) {
            if (!StringUtils.isEmpty(sf.getNotebookId())) {
                predicate.and(qNote.notebookId.equalsIgnoreCase(sf.getNotebookId()));
            }
        }

        return predicate;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    @ProtectedByAuthToken
    public Note createNote(@FormDataParam("note") FormDataBodyPart notePart) {
        notePart.setMediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE);
        Note note = notePart.getValueAs(Note.class);

        //TODO img
        note.setId(null); //ensures that new Note is created in the collection
        note = noteRepository.save(note);
        return note;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
    @Path("/{noteId}")
    @ProtectedByAuthToken
    public Note updateBlink(
            @NotBlank @PathParam("noteId") String noteId,
            @FormDataParam("note") FormDataBodyPart notePart) {
        LOGGER.info("Incoming request to update note {}", noteId);
        notePart.setMediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE);
        Note changedNote = notePart.getValueAs(Note.class);

        Note existingNote = noteRepository.findOne(noteId);

        existingNote.copy(changedNote);
        return noteRepository.save(existingNote);
    }
}
