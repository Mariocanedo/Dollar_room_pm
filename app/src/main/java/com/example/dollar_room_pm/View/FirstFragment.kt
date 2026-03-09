package com.example.dollar_room_pm.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dollar_room_pm.Model.Local.AppDatabase
import com.example.dollar_room_pm.Model.TransactionRepository
import com.example.dollar_room_pm.R
import com.example.dollar_room_pm.ViewModel.TransactionViewModel
import com.example.dollar_room_pm.ViewModel.TransactionViewModelFactory
import com.example.dollar_room_pm.databinding.FragmentFirstBinding

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    // Adapter para RecyclerView

    private val adapter = TransactionAdapter()

    // Repository y ViewModel
    private lateinit var repository: TransactionRepository

    private val viewModel : TransactionViewModel by viewModels{

        TransactionViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         // hacer las conexiones para insertar datos

        repository = TransactionRepository(AppDatabase.getDataBase(requireContext()).dollarTransactionDao())
        // mostrar en el recycler view

        binding.transactionRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.transactionRecyclerView.adapter = adapter


        // observamos los cambios en liveData  desde transacciones

        viewModel.transaction.observe(viewLifecycleOwner){


            list ->

            adapter.submitList(list)
        }
        viewModel.loadTransactions()


            // btn comprar

                 binding.buyButton.setOnClickListener { handleTransacction("BUY") }
           // btn vender

               binding.buyButton.setOnClickListener { handleTransacction("SELL") }

    }



    private fun  handleTransacction( type : String){

        val amountText = binding.amountEditText.text.toString()

        if(amountText.isEmpty()){

            Toast.makeText(requireContext(), "Ingrese un monto", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDouble()
        // guardar en la bd
        viewModel.addTransaction(type, amount)//agrega al repositorio y actualiza LiveData
        binding.amountEditText.text.clear()
        Toast.makeText(requireContext(), "$type $amount USD registrado", Toast.LENGTH_SHORT).show()

    }






    // función para ingresar transacciones de Dollar

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}