package io.github.thred.climatetray.util;

import io.github.thred.climatetray.util.prefs.Prefs;

import java.util.List;
import java.util.function.Supplier;

public interface Persistent
{

    void read(Prefs prefs);

    void write(Prefs prefs);

    public static <TYPE extends Persistent> TYPE write(Prefs prefs, TYPE object)
    {
        if (object != null)
        {
            object.write(prefs);
        }

        return object;
    }

    static <TYPE extends Persistent> void readList(Prefs prefs, String prefix, List<TYPE> list,
        Supplier<TYPE> createSupplier)
    {
        int index = 0;

        while (true)
        {
            String key = prefix + "#" + index;

            if (prefs.existsChild(key))
            {
                TYPE entry = (index < list.size()) ? list.get(index) : null;

                if (entry == null)
                {
                    entry = createSupplier.get();

                    if (index >= list.size())
                    {
                        list.add(entry);
                    }
                    else
                    {
                        list.set(index, entry);
                    }
                }

                entry.read(prefs.child(key));
            }
            else if (index < list.size())
            {
                list.remove(index);

                continue;
            }
            else
            {
                break;
            }

            index += 1;
        }
    }

    static void writeList(Prefs prefs, String prefix, List<? extends Persistent> list)
    {
        int index = 0;

        while (true)
        {
            String key = prefix + "#" + index;

            if (index < list.size())
            {
                list.get(index).write(prefs.child(key));
            }
            else if (prefs.existsChild(key))
            {
                prefs.removeChild(key);
            }
            else
            {
                break;
            }

            index += 1;
        }
    }

}
