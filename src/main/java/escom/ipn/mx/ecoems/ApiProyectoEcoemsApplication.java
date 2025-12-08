package escom.ipn.mx.ecoems;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "API ECOEMS",
                version = "1.0",
                description = "Documentación del Backend para el sistema de exámenes"
        )
)
public class ApiProyectoEcoemsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiProyectoEcoemsApplication.class, args);
    }
}