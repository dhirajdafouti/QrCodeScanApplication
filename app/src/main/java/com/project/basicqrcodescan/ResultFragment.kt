package com.project.basicqrcodescan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton


/**
 * A simple [Fragment] subclass.
 * Use the [ResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)
        val qrResult = arguments?.getString(QR_RESULt) ?: return
        view.findViewById<TextView>(R.id.qrContent).text = qrResult

        view.findViewById<MaterialButton>(R.id.scanAgainButton).setOnClickListener {
            requireActivity().onBackPressed()
        }
    }


    companion object {
        const val QR_RESULt = "qr_result"
        const val RESULT_SCANNER_FRAGMENT_TAG: String = "ResultFragmentTag"
        fun create(qrResult: String): ResultFragment {
            val resultFragment = ResultFragment()
            val args = bundleOf(
                QR_RESULt to qrResult
            )
            resultFragment.arguments = args
            return resultFragment
        }
    }

}