package com.example.fitroutine.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.example.fitroutine.R
import com.example.fitroutine.databinding.ItemOnboardingBinding

/** Datos de una pantalla del onboarding. */
data class OnboardingPage(
    @StringRes val title: Int,
    @StringRes val description: Int,
    @DrawableRes val image: Int
)

/** Las 3 pantallas del onboarding. */
val onboardingPages = listOf(
    OnboardingPage(
        title = R.string.onboarding_title_1,
        description = R.string.onboarding_desc_1,
        image = R.drawable.ic_onboarding_anywhere
    ),
    OnboardingPage(
        title = R.string.onboarding_title_2,
        description = R.string.onboarding_desc_2,
        image = R.drawable.ic_onboarding_plans
    ),
    OnboardingPage(
        title = R.string.onboarding_title_3,
        description = R.string.onboarding_desc_3,
        image = R.drawable.ic_onboarding_progress
    )
)

/** Adapter del ViewPager2: cada item es una pantalla (imagen, título y descripción). */
class OnboardingAdapter(
    private val pages: List<OnboardingPage>
) : RecyclerView.Adapter<OnboardingAdapter.PageViewHolder>() {

    class PageViewHolder(val binding: ItemOnboardingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        val page = pages[position]
        holder.binding.ivIllustration.setImageResource(page.image)
        holder.binding.tvTitle.setText(page.title)
        holder.binding.tvDescription.setText(page.description)
    }

    override fun getItemCount(): Int = pages.size
}
