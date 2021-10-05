package concerttours.service.impl;

import concerttours.daos.ProducerDAO;
import concerttours.model.ProducerModel;
import concerttours.service.ProducerService;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class DefaultProducerService implements ProducerService {

    private ProducerDAO producerDAO;

    @Override
    public List<ProducerModel> getProducers() {
        return producerDAO.findProducers();
    }

    @Override
    public ProducerModel getProducersByCode(String code) {
        final List<ProducerModel> result = producerDAO.findProducersByCode(code);
        if (result.isEmpty()) {
            throw new UnknownIdentifierException("Producer with code '" + code + "' not found!");
        }
        else if (result.size() > 1) {
            throw new AmbiguousIdentifierException("Producer code '" + code + "' is not unique, " + result.size() + " bands found!");
        }
        return result.get(0);
    }

    @Required
    public void setProducerDAO(ProducerDAO producerDAO) {
        this.producerDAO = producerDAO;
    }
}