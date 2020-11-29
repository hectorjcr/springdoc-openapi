package org.springdoc.webflux.api;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.AbstractRequestService;
import org.springdoc.core.ActuatorProvider;
import org.springdoc.core.GenericResponseService;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.OperationService;
import org.springdoc.core.SpringDocConfigProperties;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.result.method.RequestMappingInfoHandlerMapping;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_ACTUATOR_URL;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;
import static org.springframework.util.AntPathMatcher.DEFAULT_PATH_SEPARATOR;

/**
 * The type Multiple open api actuator resource.
 * @author bnasslashen
 */
@RestControllerEndpoint(id = DEFAULT_API_DOCS_ACTUATOR_URL)
public class MultipleOpenApiActuatorResource extends MultipleOpenApiResource {

	/**
	 * Instantiates a new Multiple open api resource.
	 * @param groupedOpenApis the grouped open apis
	 * @param defaultOpenAPIBuilder the default open api builder
	 * @param requestBuilder the request builder
	 * @param responseBuilder the response builder
	 * @param operationParser the operation parser
	 * @param requestMappingHandlerMapping the request mapping handler mapping
	 * @param springDocConfigProperties the spring doc config properties
	 * @param actuatorProvider the actuator provider
	 */
	public MultipleOpenApiActuatorResource(List<GroupedOpenApi> groupedOpenApis, ObjectFactory<OpenAPIService> defaultOpenAPIBuilder, AbstractRequestService requestBuilder, GenericResponseService responseBuilder, OperationService operationParser, RequestMappingInfoHandlerMapping requestMappingHandlerMapping, SpringDocConfigProperties springDocConfigProperties, Optional<ActuatorProvider> actuatorProvider) {
		super(groupedOpenApis, defaultOpenAPIBuilder, requestBuilder, responseBuilder, operationParser, requestMappingHandlerMapping, springDocConfigProperties, actuatorProvider);
	}

	/**
	 * Openapi json mono.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @param group the group
	 * @return the mono
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value =   "/{group}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<String> openapiJson(ServerHttpRequest
			serverHttpRequest, @Value(API_DOCS_URL) String apiDocsUrl,
			@PathVariable String group)
			throws JsonProcessingException {
		return getOpenApiResourceOrThrow(group).openapiJson(serverHttpRequest, apiDocsUrl + DEFAULT_PATH_SEPARATOR + group);
	}

	/**
	 * Openapi yaml mono.
	 *
	 * @param serverHttpRequest the server http request
	 * @param apiDocsUrl the api docs url
	 * @param group the group
	 * @return the mono
	 * @throws JsonProcessingException the json processing exception
	 */
	@Operation(hidden = true)
	@GetMapping(value =  "/{group}/yaml", produces = APPLICATION_OPENAPI_YAML)
	public Mono<String> openapiYaml(ServerHttpRequest serverHttpRequest,
			@Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl, @PathVariable String
			group) throws JsonProcessingException {
		return getOpenApiResourceOrThrow(group).openapiYaml(serverHttpRequest, apiDocsUrl + DEFAULT_PATH_SEPARATOR + group);
	}

}