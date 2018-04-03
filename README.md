# java-media-search-engine

Media search engines built implemented in Java using Tensorflow

### Image Search Engine

The [sample codes](src/main/java/com/github/chen0040/tensorflow/search/ImageSearchEngineDemo.java) 
below shows how to index and search for image file using the [ImageSearchEngine](src/main/java/com/github/chen0040/tensorflow/search/models/ImageSearchEngine.java) class:

```java
ImageSearchEngine searchEngine = new ImageSearchEngineInception();
if(!searchEngine.loadIndexDbIfExists()) {
    searchEngine.indexAll(FileUtils.getImageFiles("image_samples", ".png"));
    searchEngine.saveIndexDb();
}

int pageIndex = 0;
int pageSize = 20;
boolean skipPerfectMatch = true;
for(File f : FileUtils.getImageFiles("image_samples", ".png")) {
    System.out.println("querying similar image to " + f.getName());
    List<ImageSearchEntry> result = searchEngine.query(f, pageIndex, pageSize, skipPerfectMatch);
    for(int i=0; i < result.size(); ++i){
        System.out.println("# " + i + ": " + result.get(i).getPath() + " (distSq: " + result.get(i).getDistanceSq() + ")");
    }
}
```  

### Audio Search Engine

The [sample codes](src/main/java/com/github/chen0040/tensorflow/search/AudioSearchEngineDemo.java) 
below shows how to index and search for audio file using the [AudioSearchEngine](src/main/java/com/github/chen0040/tensorflow/search/models/AudioSearchEngine.java) class:

```java
AudioSearchEngine searchEngine = new AudioSearchEngine();
if(!searchEngine.loadIndexDbIfExists()) {
    searchEngine.indexAll(FileUtils.getAudioFiles("music_samples", ".au"));
    searchEngine.saveIndexDb();
}

int pageIndex = 0;
int pageSize = 20;
boolean skipPerfectMatch = true;
File f = new File("mp3_samples/example.mp3");
System.out.println("querying similar music to " + f.getName());
List<AudioSearchEntry> result = searchEngine.query(f, pageIndex, pageSize, skipPerfectMatch);
for(int i=0; i < result.size(); ++i){
    System.out.println("# " + i + ": " + result.get(i).getPath() + " (distSq: " + result.get(i).getDistanceSq() + ")");
}
```  


