# NewsRiver NewsSearch
NewsRiver NewsSearch fetches all the news using the newsriver api, with implementing the rate limiter and exponential backoff 
concurrently via multiple threads. These multiple producers pushes the news to the elasticsearch, using kafka as a queue, with multiple consumers as well. It also
implements the failure task by pushing the news to the couch database in case elastic search is not reachable and handles the exceptions and the retries to fetch all the information at its best.
