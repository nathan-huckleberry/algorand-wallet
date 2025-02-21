package com.algorand.android.ui.register

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.algorand.algosdk.mobile.Mobile
import com.algorand.android.MainNavigationDirections
import com.algorand.android.R
import com.algorand.android.core.DaggerBaseFragment
import com.algorand.android.customviews.PassphraseValidationGroupView
import com.algorand.android.databinding.FragmentPassphraseValidationBinding
import com.algorand.android.models.AnnotatedString
import com.algorand.android.models.FragmentConfiguration
import com.algorand.android.models.ToolbarConfiguration
import com.algorand.android.utils.SingleButtonBottomSheet.Companion.ACCEPT_KEY
import com.algorand.android.utils.singleVibrate
import com.algorand.android.utils.startSavedStateListener
import com.algorand.android.utils.useSavedStateValue
import com.algorand.android.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PassphraseValidationFragment : DaggerBaseFragment(R.layout.fragment_passphrase_validation) {

    private val toolbarConfiguration = ToolbarConfiguration(
        backgroundColor = R.color.tertiaryBackground,
        startIconResId = R.drawable.ic_back_navigation,
        startIconClick = ::navBack
    )

    override val fragmentConfiguration = FragmentConfiguration(
        toolbarConfiguration = toolbarConfiguration
    )

    private val binding by viewBinding(FragmentPassphraseValidationBinding::bind)

    private val args: PassphraseValidationFragmentArgs by navArgs()

    private val passphraseValidationGroupListener = object : PassphraseValidationGroupView.Listener {
        override fun onInputUpdate(allWordsSelected: Boolean) {
            binding.nextButton.isEnabled = allWordsSelected
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPassphraseValidationView()
        binding.nextButton.setOnClickListener { onNextClick() }
        initDialogSavedStateListener()
    }

    private fun getPassphraseWords(): List<String> {
        val tempAccountSecretKey = args.accountCreation.tempAccount.getSecretKey()
        return Mobile.mnemonicFromPrivateKey(tempAccountSecretKey).split(" ")
    }

    private fun setupPassphraseValidationView() {
        binding.passphraseValidationGroupView.setupUI(getPassphraseWords(), passphraseValidationGroupListener)
    }

    private fun onNextClick() {
        if (binding.passphraseValidationGroupView.isValidated()) {
            nav(
                MainNavigationDirections.actionGlobalSingleButtonBottomSheet(
                    titleResId = R.string.passphrase_verified,
                    descriptionAnnotatedString = AnnotatedString(R.string.keep_this_recovery_passphrase),
                    drawableResId = R.drawable.ic_check_sign,
                    buttonTextResId = R.string.next,
                    isResultNeeded = true
                )
            )
        } else {
            showGlobalError(errorMessage = getString(R.string.selected_words_are))
            binding.passphraseValidationGroupView.recreateUI(getPassphraseWords())
            context?.singleVibrate()
        }
    }

    private fun initDialogSavedStateListener() {
        startSavedStateListener(R.id.passphraseQuestionFragment) {
            useSavedStateValue<Boolean>(ACCEPT_KEY) {
                nav(
                    PassphraseValidationFragmentDirections
                        .actionPassphraseQuestionFragmentToAccountNameRegistrationFragment(args.accountCreation)
                )
            }
        }
    }
}
