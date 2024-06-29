# 1 Billion Records Challenge

## My Attempt

This is my attempt at the 1 Billion Records Challenge. The code is inspired by Rene Schwietzke's
implementation: [1brc-the-first-80-meters](https://github.com/rschwietzke/1brc-the-first-80-meters).

## Benchmark Results

The benchmarks were run on an AMD Ryzen 5800x3D processor. The results are as follows:

| Class Name          | Time [ms] | Improvement |
|---------------------|-----------|-------------|
| BaselineAggregator  | 198129    | 100.00%     |
| IndexOfAggregator   | 147982    | 74.69%      |
| GetAndPutAggregator | 149975    | 75.70%      |
| MinMaxAggregator    | 142715    | 72.03%      |
| UseIntAggregator    | 136765    | 69.03%      |
| FastSetAggregator   | 131601    | 66.42%      |
| ChannelAggregator   | 74399     | 37.55%      |
| NoStringAggregator  | 57057     | 28.80%      |
| NoCopyAggregator    | 37785     | 19.07%      |

## References

- [Rene Schwietzke's 1 Billion Records Challenge Implementation](https://github.com/rschwietzke/1brc-the-first-80-meters)
