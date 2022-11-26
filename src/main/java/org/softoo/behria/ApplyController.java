package org.softoo.behria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("/apply")
public class ApplyController {

	private static final String BANK_URL = "http://localhost:9090";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/ping")
	public String ping() {
		return "Pong";
	}
	
	@PostMapping
	public ResponseEntity<?> applyForBS(@RequestBody ApplyDTO dto) {
		FeeDTO feeDTO = new FeeDTO(dto.getId(), "UNIVERSITY_TOKEN_NUMBER", dto.getFee());
		ResponseEntity<FeeResponseDTO> response = this.restTemplate.postForEntity(BANK_URL + "/api/v1/feee", feeDTO, FeeResponseDTO.class);
		if(response.getStatusCode() == HttpStatus.OK) {
			
			// fee paid
			// eligible entry test
			
			return new ResponseEntity<FeeResponseDTO>(response.getBody(), HttpStatus.OK);
		}
		
		return new ResponseEntity<Object>(null, HttpStatus.BAD_GATEWAY);
	}
	
	@Data
	public static class ApplyDTO {
		String id;
		int fee;
	}
	
	@Data
	@AllArgsConstructor
	public static class FeeDTO {
		private String studentId;
		private String tokenNumber;
		private int amount;
	}
	
	@Data
	public static class FeeResponseDTO {
		private String studentId;
		private boolean success;
	}
	
}
