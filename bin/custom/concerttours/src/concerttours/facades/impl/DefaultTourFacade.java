package concerttours.facades.impl;

import concerttours.data.ProducerData;
import concerttours.facades.ProducerFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.variants.model.VariantProductModel;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import concerttours.data.ConcertSummaryData;
import concerttours.data.TourData;
import concerttours.enums.ConcertType;
import concerttours.facades.TourFacade;
import concerttours.model.ConcertModel;

public class DefaultTourFacade implements TourFacade {
    private ProductService productService;
    private ProducerFacade producerFacade;

    @Override
    public TourData getTourDetails(final String tourId) {
        if (tourId == null) {
            throw new IllegalArgumentException("Tour id cannot be null");
        }
        final ProductModel product = productService.getProductForCode(tourId);
        if (product == null) {
            return null;
        }
        final List<ConcertSummaryData> concerts = new ArrayList<>();
        if (product.getVariants() != null) {
            for (final VariantProductModel variant : product.getVariants()) {
                if (variant instanceof ConcertModel) {
                    final ConcertModel concert = (ConcertModel) variant;
                    final ConcertSummaryData summary = new ConcertSummaryData();
                    summary.setId(concert.getCode());
                    summary.setDate(concert.getDate());
                    summary.setVenue(concert.getVenue());
                    summary.setType(concert.getConcertType() == ConcertType.OPENAIR ? "Outdoors" : "Indoors");
                    summary.setCountDown(concert.getDaysUntil());
                    concerts.add(summary);
                }
            }
        }
        ProducerData producerData = producerFacade.getProducer(product.getProducer().getCode());
        final TourData tourData = new TourData();
        tourData.setId(product.getCode());
        tourData.setTourName(product.getName());
        tourData.setDescription(product.getDescription());
        tourData.setConcerts(concerts);
        tourData.setProducer(producerData);
        return tourData;
    }

    @Required
    public void setProductService(final ProductService productService) {
        this.productService = productService;
    }

    @Required
    public void setProducerFacade(final ProducerFacade producerFacade) {
        this.producerFacade = producerFacade;
    }
}