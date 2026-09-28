# OMDb CLI — Program Flow

## OVERALL APPLICATION FLOW

STEP 1
Program starts in Main.main().

STEP 2
Main reads OMDB_API_KEY from the environment.

STEP 3
Main creates Scanner for user input.

STEP 4
Main enters the PROGRAM LOOP.

STEP 5
Program asks the user for a movie search term.

STEP 6
Main creates/uses OmdbClient to communicate with OMDb.

STEP 7
Program enters the SEARCH LOOP.

STEP 8
Main asks OmdbClient for the current search page.

--------------------------------------------------
## STEP 8 — OmdbClient.searchMovies()
--------------------------------------------------

STEP 8A
Receive search term and page number.

STEP 8B
URL-encode the search term.

STEP 8C
Build the OMDb search URL.

STEP 8D
Create URI.

STEP 8E
Create HTTP GET request.

STEP 8F
HttpClient sends the request.

STEP 8G
Receive JSON response.

STEP 8H
Check HTTP status.

STEP 8I
Send JSON to parseSearchResults().

--------------------------------------------------
## STEP 8I — parseSearchResults()
--------------------------------------------------

STEP 8I-A
Convert JSON String into JsonNode.

STEP 8I-B
Check OMDb Response.

STEP 8I-C
Read Search[].

STEP 8I-D
Read totalResults.

STEP 8I-E
Convert each JSON movie into SearchResult.

STEP 8I-F
Store SearchResult objects in a List.

STEP 8I-G
Create SearchResultPage using:
        List<SearchResult>
        totalResults

STEP 8I-H
Return SearchResultPage to Main.

--------------------------------------------------
## BACK TO MAIN
--------------------------------------------------

STEP 9
Main extracts results from SearchResultPage.

STEP 10
Main extracts totalResults from SearchResultPage.

STEP 11
Main calculates totalPages.

STEP 12
Main displays the current page.

STEP 13
User chooses an option.

N
→ increase page
→ SEARCH LOOP repeats

P
→ decrease page
→ SEARCH LOOP repeats

Q
→ leave SEARCH LOOP

Movie number
→ select SearchResult

--------------------------------------------------
## MOVIE DETAILS
--------------------------------------------------

STEP 14
Main gets IMDb ID from selected SearchResult.

STEP 15
Main calls OmdbClient.getMovieById(imdbId).

STEP 16
OmdbClient builds the movie-details request.

STEP 17
OmdbClient sends the request.

STEP 18
OMDb returns detailed JSON.

STEP 19
parseMovie() processes the JSON.

STEP 20
Jackson converts JSON into Movie.

STEP 21
Movie object is returned to Main.

STEP 22
Main displays Movie details.

STEP 23
User returns to the SEARCH LOOP.

--------------------------------------------------
## NEW SEARCH
--------------------------------------------------

STEP 24
User chooses New Search.

STEP 25
SEARCH LOOP ends.

STEP 26
PROGRAM LOOP starts again.

STEP 27
Program asks for a new movie search term.

STEP 28
New search begins from page 1.