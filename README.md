# Voting application to test lgtm capabilities

To Generate some traffic, run the following command:

```bash
  while :; do http :8080/vote candidateId=$(python3 -c "import random; print(random.randint(1,3))"); sleep 1; done 
```

