package pt.nunopeixoto.wtest.db.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.flow.Flow
import pt.nunopeixoto.wtest.db.entity.PostalCode


@Dao
interface PostalCodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostalCodeList(postalCodeList: List<PostalCode>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostalCode(postalCode: PostalCode)

    @RawQuery(observedEntities = [PostalCode::class])
    fun getSearchQueryPostalCodes(searchQuery: SimpleSQLiteQuery): Flow<List<PostalCode>>

    @Query("SELECT * FROM postalcode")
    fun getAllPostalCodes(): List<PostalCode>
}