package edu.tamu.app.controller;

import edu.tamu.framework.aspect.annotation.ApiMapping;
import edu.tamu.framework.model.ApiResponse;
import static edu.tamu.framework.enums.ApiResponseType.SUCCESS;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiMapping("/status")
public class StatusController {
	
	@ApiMapping("/overall")
	public ApiResponse overall() {
		
		Map<String, String> map = new HashMap<String,String>();
		
		map.put("message", "The service says everything is not ok!");
		map.put("type", "ERROR");
		
        return new ApiResponse(SUCCESS, map);
	}
	
}
