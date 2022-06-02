# rev-contract-back
a back end for contract-style grading

## endpoints

- `/users`
    - `POST`: registering an associate account.
```
{
    "id":number,
    "firstName":string,
    "lastName":string,
    "rubrics":[],
    "actualScores":[]
}
```

- `/users/code`
    - `POST`: retrieve an associate.
```
{
    "firstName":string,
    "lastName":string,
    "code":string
}
```

- `/scores/{associateId}`
    - `GET`: retrieve a score summary by associate.
    - `POST`: submit a score for an associate.
```
{
    "id":number,
    "rubricTheme":string,
    "value":number,
    "week":number,
    "note":string
}
```

- `/rubrics`
    - `GET`: retrieve all of the available rubric themes.

- `/rubrics/{associateId}`
    - `POST`: submit a custom rubric for a particular associate.
```
{
    "id":number,
    "rubricTheme":{
        "id":number,
        "theme":string,
        "description":string
    },
    "score":number, // the score that this rubric is describing
    "description":string
}
```