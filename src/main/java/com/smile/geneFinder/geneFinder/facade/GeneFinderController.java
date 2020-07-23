package com.smile.geneFinder.geneFinder.facade;
import com.smile.geneFinder.geneFinder.business.GeneFinderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneFinderController {

    GeneFinderService geneSearchService;

    public GeneFinderController(GeneFinderService geneSearchService) {
        this.geneSearchService = geneSearchService;
    }

    @GetMapping("/genes/find/{gen}")
    public ResponseEntity<String> geneSearch(@PathVariable("gen") String gene) {
        String responseMessage;
        HttpStatus responseStatus;
        if (!this.geneSearchService.isReady()) {
            responseMessage = "Server is NOT ready. Still indexing...";
            responseStatus = HttpStatus.SERVICE_UNAVAILABLE;
        }
        else {
            if (!geneSearchService.isValidGene(gene)) {
                responseMessage = "Not valid gene";
                responseStatus = HttpStatus.BAD_REQUEST;
            }
            else {
                if (geneSearchService.isMatch(gene)) {
                    responseMessage = "Gene IS in DNA file";
                    responseStatus = HttpStatus.OK;
                }
                else {
                    responseMessage = "Gene NOT in DNA file";
                    responseStatus = HttpStatus.NOT_FOUND;
                }
            }
        }

        return new ResponseEntity<>(responseMessage, responseStatus);
    }
}

