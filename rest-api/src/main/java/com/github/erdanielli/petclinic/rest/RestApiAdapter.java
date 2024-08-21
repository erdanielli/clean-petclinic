package com.github.erdanielli.petclinic.rest;

import com.github.erdanielli.petclinic.application.vet.UcListVets;
import com.github.erdanielli.petclinic.application.vet.fake.UcListVetsFake;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestApiAdapter {

    // TODO: remove scratch
    @Bean
    UcListVets ucListVetsFake() {
        return new UcListVetsFake();
    }

    public static void main(String[] args) {
        SpringApplication.run(RestApiAdapter.class, args);
    }

}
