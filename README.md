SimplePx
========

A native android app that provides immersive browsing experience of 500px photos.

## Activities

There are three activities in total in this app: StartActivity, MainActivity and PhotoActivity.

StartActivity: The purpose of the StartActivity is to display something when the app is loading
the first batch of photos.

MainActivity: The main grid view of the app. The gird view in this activity is implemented with
RecyclerView.

PhotoActivity: Fullscreen paged UI.

StartActivity -> MainActivity <-> PhotoActivity

## Library used

Square Stack: okhttp, retrofit, picasso. These two libraries all have very simple and clean
interfaces. They all help me of writing simpler and cleaner code.

Dagger & ButterKnife: These two library are used for eliminating boilerplate code, so that I don't
need to rewrite singleton instantiation and findViewById in every class I created.

RxJava & RxAndroid: These library are used for async network calls. They're supported by retrofit.

## Implementation details and decisions by parts

Part 1, 2, 4 are omitted as they are all implemented in the later parts.

### Part 3: Make the thumbnail grid adaptive to the size and orientation of the device

The number of spans in the RecyclerView is calculated according to the size of the screen and
photo(440px)

### Part 5 & Part 8 Full-screen infinite pager UI

Android has a ViewPager widget that supports most of the desired functionalities, but it was
originally designed for the transition between fragments with finite pages.

I searched for some libraries that support infinite pages on Github,
but most of them overcomplicate the problem. Thus, I wrote a PagerAdapter that implements infinite
pages.

### Part 6: Animate transition between thumbnail and full-screen photo

I think this part is the hardest of the project, and I spent most of time on this part as well.

Since I chose to use another activity (PhotoActivity) for displaying full screen photos, activity
transition (Which was add in Lolipop(API 21)) is used. The good thing about activity transition is
that it is very simple to use; all I need was couple of lines of code.

But the downside is that it is very hard to modify the internal transition mechanism, since most of
related instances are private fields in the Activity class, and these instance's implementations are
not public. At the end, I managed to animate the enter transition, but the reenter transition back
to the MainActivity was not behaving correctly. The reenter transition by default is disabled, it
can be enabled from the debug settings on the right drawer in the MainActivity.

I would love to talk more about this part if I'm invited for an on-site interview.

### Part 7: Allow infinite scroll of thumbnails

Since the we want to share the "state" of photos in both grid view and full screen view, I created a
PhotoProvider that wraps around a list of photos. In addition, PhotoProvider handles most of the
API calls. It hides the paging of the API from the UI code.

## Reflection

In the process of implementing this project, I noticed that memory and performances are very
important in a photo app. Due to the nature of the photos (Bitmap), a large fullscreen photo may
take all of heap ram for a app. To enhance the user experience, loading time of high resolution
photos also needs to be minimized.

