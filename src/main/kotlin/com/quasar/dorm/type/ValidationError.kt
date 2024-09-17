package com.quasar.dorm.type
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
class ValidationError(val violations : List<TypeViolation>) : Error() {}