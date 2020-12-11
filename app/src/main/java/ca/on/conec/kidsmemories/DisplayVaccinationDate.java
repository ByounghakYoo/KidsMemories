package ca.on.conec.kidsmemories;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class DisplayVaccinationDate implements DayViewDecorator{
    private  final int color;
    private final HashSet<CalendarDay> dates;

    public DisplayVaccinationDate(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains((day));
    }

    @Override
    public void decorate(DayViewFacade view) {
       // view.addSpan(new DotSpan(5, color));

        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new DotSpan(10,color));
        view.addSpan(new ForegroundColorSpan(Color.RED));
        view.addSpan(new RelativeSizeSpan(1.4f));
    }
}
