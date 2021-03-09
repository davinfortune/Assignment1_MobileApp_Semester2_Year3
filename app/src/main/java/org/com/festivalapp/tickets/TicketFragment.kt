package org.com.festivalapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class TicketFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket, container, false)

        val addTicketButton : Button = view.findViewById(R.id.addTicketButton)
        addTicketButton.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.ticketLayout, AddTicketFragment()).commit()
        }
        return view
    }
}
