package org.sirius.dorm.service
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */

import org.sirius.dorm.model.ObjectDescriptor
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("dorm/")
@Component
class DORMAdministrationService : AbstractDORMService() {
    // meta data methods

    @GetMapping("/read-descriptor/{type}")
    @ResponseBody
    fun readDescriptor(@PathVariable type: String): ObjectDescriptor {
        return withTransaction {
            return@withTransaction objectManager.getDescriptor(type)
        }
    }

    /*
    @PostMapping("/update-descriptor")
    @ResponseBody
    fun updateDescriptor(@RequestBody descriptor: ObjectDescriptor): ObjectDescriptor {
        return withTransaction {
            return@withTransaction objectManager.updateDescriptor(descriptor)
        }
    }

    @DeleteMapping("/delete-descriptor/{type}")
    @ResponseBody
    fun deleteDescriptor(@PathVariable type: String): ObjectDescriptor {
        return withTransaction {
            return@withTransaction objectManager.deleteDescriptor(type)
        }
    }*/
}