package com.example.cafe_backedn.controller;

import com.example.cafe_backedn.advices.ApiError;
import com.example.cafe_backedn.advices.ApiResponse;
import com.example.cafe_backedn.advices.PaginationResponse;
import com.example.cafe_backedn.dto.CategoryDto;
import com.example.cafe_backedn.dto.OrderDto;
import com.example.cafe_backedn.entity.OrderEntity;
import com.example.cafe_backedn.services.OrderService;
import com.example.cafe_backedn.services.PdfGeneratorService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final PdfGeneratorService pdfGeneratorService;
    private final OrderService orderService;

    @PostMapping(value = "/store",produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Object> storeOrder(@Valid @RequestBody OrderDto body){
        try{
            OrderDto saveOrder = orderService.storeOrder(body);
            System.out.println("the save="+saveOrder.getId());
            byte[] pdfBytes = pdfGeneratorService.generatePdf(saveOrder.getId());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
//        return ResponseEntity.ok(saveOrder);
        }catch (Exception ex){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> allOrders(){
        List<OrderDto> allOrder = orderService.allOrder();
        return ResponseEntity.ok(allOrder);
    }

    @GetMapping("/count")
    public Object getOrderCount(){
        Map<String, Object> response = new HashMap<>();
        response.put("count", orderService.getCountCategory());
        return response;
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOneOrder(@PathVariable Long id){
        OrderDto order = orderService.getOneOrder(id);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteOrder(@PathVariable Long id){
        boolean update = orderService.updateOrder(id);
        return update;
    }

    @GetMapping("/orders")
    public ResponseEntity<PaginationResponse<OrderDto>> getOrder(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        PaginationResponse<OrderDto> response = orderService.getPaginatedOrder(pageable,keyword);
        return ResponseEntity.ok(response);
    }
}
