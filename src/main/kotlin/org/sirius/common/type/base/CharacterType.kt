package org.sirius.common.type.base
/*
 * @COPYRIGHT (C) 2023 Andreas Ernst
 *
 * All rights reserved
 */
import org.sirius.common.type.Type

class CharacterType : Type<Char>(Char::class.javaObjectType) {
}

fun character() : CharacterType {return CharacterType()
}