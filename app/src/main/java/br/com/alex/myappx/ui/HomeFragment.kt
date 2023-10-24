package br.com.alex.myappx.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.alex.myappx.R
import br.com.alex.myappx.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: FirebaseDatabase
    private lateinit var myReference: DatabaseReference




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = binding.btnSwitch
        val imageView = binding.ledStatus

        button.setOnCheckedChangeListener { compoundButton, onSwitch ->
            if (onSwitch) {
                ligar()
                imageView.setImageResource(R.drawable.ic_led_on)

            }else{
                deligar()
                imageView.setImageResource(R.drawable.ic_led_off)
            }
        }
        // Chamar a função getDHTData aqui
        getDHTData()

    }

    private fun ligar() {
        var ligar: Int = 1

        myReference = FirebaseDatabase.getInstance().getReference("Led")
        myReference.child("Status").setValue(ligar).addOnSuccessListener {
            Toast.makeText(requireContext(), "LED Ligado!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(requireContext(), "Falha ao inserir dados!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun deligar() {
        var desligar: Int = 0

        myReference = FirebaseDatabase.getInstance().getReference("Led")
        myReference.child("Status").setValue(desligar).addOnSuccessListener {
            Toast.makeText(requireContext(), "LED Desligado!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(requireContext(), "Falha ao inserir dados!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getDHTData() {
        myReference = FirebaseDatabase.getInstance().getReference("DHT11")

        val tempListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val temperature = dataSnapshot.child("Temperatura").getValue(Float::class.java)
                println("Temperatura: $temperature")
                binding.txtTemperatura.text = temperature.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to read temperature.")
            }
        }

        val humListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val humidity = dataSnapshot.child("Umidade").getValue(Float::class.java)
                println("Umidade: $humidity")
                binding.txtUmidade.text = humidity.toString()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to read humidity.")
            }
        }

        myReference.addValueEventListener(tempListener)
        myReference.addValueEventListener(humListener)
    }


}