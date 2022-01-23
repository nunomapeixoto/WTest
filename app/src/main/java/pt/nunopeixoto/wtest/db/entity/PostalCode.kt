package pt.nunopeixoto.wtest.db.entity

import androidx.room.Entity

/**
 * @property normalizedName usado para guardar o texto normalizado dos nomes das localidades para
 * poder pesquisar com ou sem os acentos a partir desta tabela na BD
 */
@Entity(primaryKeys = ["code", "extCode"])
data class PostalCode(
    val name: String,
    val normalizedName: String,
    val code: String,
    val extCode: String
)
