package com.springinventory.demo.controller;

import com.springinventory.demo.exception.ResourceNotFoundException;
import com.springinventory.demo.model.Item;
import com.springinventory.demo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
public class ItemController {
/**
 * todo:
 *
 * */

    @Autowired
    private ItemRepository itemRepository;

    /** returns a list of all items */
    @GetMapping("/items")
    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    @GetMapping("/items/{itemNo}")
    public ResponseEntity <Item> getItemById(@PathVariable(value = "itemNo") Long itemNumber)
        throws ResourceNotFoundException {

        // find item by id, throw resource not found in case one with provided id was not found
        Item item = itemRepository.findById(itemNumber)
                .orElseThrow(() ->new ResourceNotFoundException("Item not found for this id :: " + itemNumber));

        // reply with the found item
        return ResponseEntity.ok().body(item);
    }

    /**
     * withdraw n items  - if there aren't enough don't withdraw and return a message
    **/
    @PutMapping("/items/withdraw/{itemNo}/{quantity}")
    public ResponseEntity<Map> withdrawById(@PathVariable(value = "itemNo") Long itemNumber, @PathVariable(value = "quantity") Long n)
            throws ResourceNotFoundException {

        // find item by id, throw resource not found in case one with provided id was not found
        Item item = itemRepository.findById(itemNumber)
                .orElseThrow(() ->new ResourceNotFoundException("Item not found for this id :: " + itemNumber));


        // response object
        Map response =  new HashMap<String, String>();
        // verify sufficient quantity
        long existingQuantity = item.getQuantity();
        response.put("Item Number", item.getItemNumber());

        if(existingQuantity < n)
            response.put("Error", "Insufficient quantity for supplied item");
        else
            response.put("Item Quantity", item.getQuantity());

        return  ResponseEntity.ok(response);
    }

    /**
     * @function depositById - deposit n > 0 items of item no.
     * @param itemNumber - inventory number of deposited item\s
     * @param n - quantity of items to deposit
     * @throws ResourceNotFoundException in case that item by provided number does not exist
     * @return {"item number": itemNumber, "item quantity": itemQuantity}
     **/
    @PutMapping("/items/deposit/{itemNo}/{quantity}")
    public ResponseEntity<Map> depositById(@PathVariable(value = "itemNo") Long itemNumber, @PathVariable(value = "quantity") @Min(1) Long n)
            throws ResourceNotFoundException {

        // find item by id, throw resource not found in case one with provided id was not found
        Item item = itemRepository.findById(itemNumber)
                .orElseThrow(() ->new ResourceNotFoundException("Item not found for this id :: " + itemNumber));

        // update quantity
        long newQuantity = item.getQuantity() + n;
        item.setQuantity(newQuantity);
        //persist
        itemRepository.save(item);
        // response object
        Map response =  new HashMap<String, String>();
        // verify sufficient quantity
        long existingQuantity = item.getQuantity();
        response.put("item number", item.getItemNumber());
        response.put("item quantity", newQuantity);

        return  ResponseEntity.ok(response);
    }

    // Crate new item
    // Todo: change response to: { created: "true, message: "item created", item}
    @PostMapping("/items")
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item _item) {

        @Valid Item savedItem = itemRepository.save(_item);

        return ResponseEntity.ok(savedItem);

    }

    // Update item properties
    @PutMapping("/items/{id}")
    public ResponseEntity<Map> updateItem(@PathVariable(value = "id") Long itemId,
                                           @Valid @RequestBody Item itemDetails) throws ResourceNotFoundException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));

        // update item properties according to provided data
        item.setName(itemDetails.getName());
        item.setDescription(itemDetails.getDescription());
        item.setInventoryCode(itemDetails.getInventoryCode());// Todo: see how a duplicate is handled

        final Item updatedItem = itemRepository.save(item);
        Map<String, Object> response = new HashMap();
        response.put("updated", Boolean.TRUE);
        response.put("message", "message updated");
        response.put("item", updatedItem);
        return ResponseEntity.ok(response);
    }

    // delete item
    // Todo: response: {deleted: "true", item} <- addition of the item to the response for undo functionality
    @DeleteMapping("/items/{id}")
    public Map <String, Boolean> deleteItem(@PathVariable(value = "id") Long itemId)
    throws ResourceNotFoundException {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new ResourceNotFoundException("Employee not found for this id :: " + itemId));

        itemRepository.delete(item);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}
