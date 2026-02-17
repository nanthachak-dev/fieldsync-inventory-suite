package com.example.fieldsync_inventory_app.domain.repository.transaction_operation

import android.util.Log
import com.example.fieldsync_inventory_app.data.remote.api.StockTransactionOperationApi
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.TransactionOperationCreateReqDto
import com.example.fieldsync_inventory_app.data.remote.dto.transaction_operation.request.stock_in.TransactionOperationStockInReqDTO
import retrofit2.HttpException
import javax.inject.Inject

class TransactionOperationRepositoryImpl @Inject constructor(
    private val api: StockTransactionOperationApi
): TransactionOperationRepository {
    override suspend fun saveTransactionOperation(reqDto: TransactionOperationCreateReqDto) {
        try {
            //Log.d("TransactionOperationRepositoryImpl", "Trying to create Transaction Operation: $reqDto")
            val response = api.createTransactionOperation(reqDto)
            Log.d("TransactionOperationRepositoryImpl", "Create Transaction Operation succeed with transaction_id: ${response.transactionId}")
        } catch (e: HttpException) { // Show error response from backend
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("TransactionOperationRepositoryImpl", "Create Transaction Operation failed: $errorBody")
            throw Exception(errorBody)
        } catch (e: Exception){
            Log.e("TransactionOperationRepositoryImpl", "Create Transaction Operation failed: ${e.message}")
            throw e
        }
    }

    override suspend fun saveTransactionOperationStockIn(reqDto: TransactionOperationStockInReqDTO) {
        try {
            Log.d("TransactionOperationRepositoryImpl.saveTransactionOperationStockIn", "Trying to create transaction operation for stock-in with request data: $reqDto")
            val response = api.createTransactionOperationStockIn(reqDto)
            Log.d("TransactionOperationRepositoryImpl.saveTransactionOperationStockIn", "Create transaction operation for stock-in succeed with transaction_id: ${response.transactionId}")
        }catch (e: HttpException){
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("TransactionOperationRepositoryImpl.saveTransactionOperationStockIn", "Create transaction operation for stock-in failed: $errorBody")
            throw Exception(errorBody)
        }catch (e: Exception){
            Log.e("TransactionOperationRepositoryImpl.saveTransactionOperationStockIn", "Create transaction operation for stock-in failed: ${e.message}")
            throw e
        }
    }

    override suspend fun deleteTransactionOperation(id: Long) {
        try {
            Log.d("TransactionOperationRepositoryImpl", "Trying to delete Transaction Operation with id: $id")
            api.deleteTransactionOperation(id)
            Log.d("TransactionOperationRepositoryImpl", "Delete Transaction Operation succeed for id: $id")
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.e("TransactionOperationRepositoryImpl", "Delete Transaction Operation failed: $errorBody")
            throw Exception(errorBody)
        } catch (e: Exception) {
            Log.e("TransactionOperationRepositoryImpl", "Delete Transaction Operation failed: ${e.message}")
            throw e
        }
    }
}
