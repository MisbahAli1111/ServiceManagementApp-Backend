package com.qavi.carmaintanence.usermanagement.controllers.role;

import com.qavi.carmaintanence.usermanagement.entities.role.Role;
import com.qavi.carmaintanence.usermanagement.models.PermissionBitsModel;
import com.qavi.carmaintanence.usermanagement.models.ResponseModel;
import com.qavi.carmaintanence.usermanagement.models.RoleModel;
import com.qavi.carmaintanence.usermanagement.services.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/roles")
public class RoleController {
    @Autowired
    RoleService roleService;
    @PostMapping("/add-role")
    public ResponseEntity<ResponseModel> addRole(@RequestBody Role role){
        ResponseModel responseModel=ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Role Added Successfully")
                .data(new Object())
                .build();
        if(!roleService.addRole(role))
        {
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
            responseModel.setMessage("Failed To Add Role");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
    @PostMapping("/{roleId}/assign-permission")
    public ResponseEntity<ResponseModel> assignPermission(@RequestBody PermissionBitsModel permissionBitsModel, @PathVariable Long roleId)
    {
        ResponseModel responseModel=ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Permission Assigned Successfully")
                .data(new Object())
                .build();
        if(!roleService.assignPermission(permissionBitsModel,roleId))
        {
            responseModel.setMessage("Failed To Assign Permission");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
    @DeleteMapping("/{roleId}/delete-role")
    public ResponseEntity<ResponseModel> deleteRoles(@PathVariable Long roleId)
    {
        ResponseModel responseModel=ResponseModel.builder()
                .status(HttpStatus.OK)
                .message("Role has been Deleted Successfully")
                .data(new Object())
                .build();
        if(!roleService.deleteRoles(roleId))
        {
            responseModel.setMessage("Failed To Delete Role");
            responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }
@PostMapping("/{Role_ID}/update_role")
public  ResponseEntity<ResponseModel> update_role(@PathVariable Long Role_id  , @RequestBody RoleModel roleModel) {
    ResponseModel responseModel = ResponseModel.builder()
            .status(HttpStatus.OK)
            .message("Role Updated Successfully")
            .data(new Object())
            .build();
    if (!roleService.update_role(Role_id, roleModel)) {
        responseModel.setMessage("Role Updation Failed");
        responseModel.setStatus(HttpStatus.EXPECTATION_FAILED);
    }

        return ResponseEntity.status(HttpStatus.OK).body(responseModel);


}
}
