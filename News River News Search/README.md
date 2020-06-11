# NewsRiver NewsSearch
NewsRiver NewsSearch fetches all the news using the newsriver api, with implementing the rate limiter and exponential backoff 
concurrently via multiple threads. These multiple producers pushes the news to the elasticsearch, using kafka as a queue, with multiple consumers as well. 

It also implements the failure task by pushing the news to the couch database in case elastic search is not reachable and handles the exceptions and the retries to fetch all the information at its best.

The elastic search can then be used to search the news directly.

Have a look here, to see it actually [working](https://drive.google.com/file/d/1x7bQ0kUk_VWunTR6KyAAq8ePs7hYc-sL/view?usp=sharing)    
