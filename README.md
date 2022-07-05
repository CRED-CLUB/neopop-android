# NeoPop
NeoPop is CRED's inbuilt library for using NeoPop components in your app.

What really is NeoPop? NeoPop was created with one simple goal, to create the next generation of the next beautiful, more affirmative, design system. neopop stays true to everything that design at CRED stands for.


![Banner](https://i.imgur.com/1gN3wzy.jpg "Banner")


## Install
You can install NeoPop by adding these to your project:

1. Add this to your module `build.gradle` file:

```groovy
dependencies {  
  implementation 'club.cred:neopop:1.0.0'
}
```


2. Add this to the root/project `build.gradle` file:

```groovy
allprojects {
  repositories {
    google()
    jcenter()

    maven {
        url = 'https://libs.dev.cred.club/'
    }
  }
}
```


# PopLayout
![Configs](https://user-images.githubusercontent.com/9965653/173539706-fa521743-b214-4372-87dd-799d9b8b6c70.png)
`PopFrameLayout` render 5 surfaces, top, left, right, bottom and center. 
These surfaces can be customized in two ways:

## Elevated
![Elevated](https://user-images.githubusercontent.com/9965653/172596228-1bcb92d0-d293-4290-ac38-b9a693a2fab2.png "Elevated Button")
![elevated](https://user-images.githubusercontent.com/9965653/175874614-ab316981-29d1-4ed5-a90b-6fe840ac9700.gif)

By specifying the `button_position` as `bottom|right`, neopop will compute bottom and right surface's color according to `neopop_center_surface_color` . It will also compute visibility of the surfaces according to `button_position`
```xml  
<club.cred.neopop.PopFrameLayout  
  ... 
  android:clickable="true" 
  app:neopop_button_position="bottom|right" 
  app:neopop_center_surface_color="@color/white"
  app:neopop_depth="3dp" 
  app:neopop_parent_view_color="@color/black"/>  
```  

## Flat
![Flat](https://user-images.githubusercontent.com/9965653/172597180-63b4c386-9b7c-4211-a64c-a79892232481.png "Flat Button")
![flatButton](https://user-images.githubusercontent.com/9965653/175874619-b5edb3e8-6c18-4f15-8e60-ef44cb004c94.gif)
By specifying the `button_position` as `center`, neopop will compute right and bottom surface's color according to `neopop_center_surface_color`.
Top and left surface's color is computed w.r.t `neopop_parent_view_color` and `neopop_grandparent_view_color`
```xml  
<club.cred.neopop.PopFrameLayout
  ...  
  android:clickable="true"
  app:neopop_parent_view_color="@color/black"  
  app:neopop_button_position="center"
  app:neopop_center_surface_color="@color/white"
  app:neopop_depth="3dp"/>  
```  

## Shimmer

```xml  
<club.cred.neopop.PopFrameLayout    
  ...  
  app:neopop_shimmer_duration="5000" 
  app:neopop_shimmer_width="24dp"    
  app:neopop_shimmer_color="#f00"    
  app:neopop_show_shimmer="true"/>  
 ```  
## Flat Strokes
![Flat Strokes](https://user-images.githubusercontent.com/9965653/172597728-5830cc72-1d2a-4d52-8089-55fb61449996.png "Flat Strokes")
![flatStroked](https://user-images.githubusercontent.com/9965653/175874617-a90ef305-d460-4887-927c-0ddecfe45975.gif)
To render stroke on a flat button, add `neopop_stroke_color`
```xml  
<club.cred.neopop.PopFrameLayout    
   ...  
   app:neopop_button_position="center"
   app:neopop_draw_full_height="true"    
   app:neopop_draw_full_width="true"    
   app:neopop_parent_view_color="@color/black"  
   app:neopop_stroke_color="#f00">  
```  

## Plunk Strokes

![Elevated Strokes](https://user-images.githubusercontent.com/9965653/172597473-630c86b9-574d-4f65-afeb-171c5ec147cc.png "Elevated Strokes")
![elevatedStroked](https://user-images.githubusercontent.com/9965653/175874610-3e92e9e9-cbed-4906-a90b-423c615e465d.gif)
To render strokes on an elevated button ,add
`neopop_is_stroked_button = "true"` and `neopop_stroke_color`
```xml  
<club.cred.neopop.PopFrameLayout    
   ...  
   android:clickable="true"
   app:neopop_button_position="bottom|right" 
   app:neopop_bottom_surface_color="#0f0"
   app:neopop_right_surface_color="#0f0" 
   app:neopop_top_surface_color="@android:color/transparent"
   app:neopop_left_surface_color="@android:color/transparent"    
   app:neopop_is_stroked_button="true"    
   app:neopop_stroke_color="#0f0">  
```  

## Adjacent Buttons

![Adjacent Buttons](https://user-images.githubusercontent.com/9965653/174827283-4ae73c48-eba8-41ce-9661-30924aef2d3c.png)

### Horizontally aligned buttons
![Horizontally aligned](https://user-images.githubusercontent.com/9965653/177260584-04525dca-5386-4b9a-bf2e-acb607823b02.gif)
```xml  
<Space
  android:id="@+id/left_space"
  android:layout_width="3dp"
  android:layout_height="0dp"
  app:layout_constraintBottom_toBottomOf="parent"
  app:layout_constraintStart_toStartOf="@id/center_top"
  app:layout_constraintTop_toTopOf="parent" />

<club.cred.neopop.PopFrameLayout
  ...
  android:id="@+id/left_top"
  android:clickable="true"
  app:layout_constraintEnd_toEndOf="@id/left_space"
  app:layout_constraintTop_toTopOf="@id/center_top"
  app:neopop_button_on_right="@id/center_top">
  ...content
</club.cred.neopop.PopFrameLayout>


<club.cred.neopop.PopFrameLayout
  ...
  android:id="@+id/center_top"
  android:clickable="true"
  app:neopop_button_on_left="@id/left_top"
  app:neopop_center_surface_color="@color/white">
  ...content
</club.cred.neopop.PopFrameLayout>
```  
### Vertically Aligned buttons
![Vertically Aligned Button](https://user-images.githubusercontent.com/9965653/177260491-e017a368-4556-4368-9709-7b6897756675.gif)
```xml 
<Space
  android:id="@+id/bottom_space"
  android:layout_width="0dp"
  android:layout_height="3dp"
  app:layout_constraintBottom_toBottomOf="@id/right_top" />

<club.cred.neopop.PopFrameLayout
  ...
  android:id="@+id/right_top"
  android:clickable="true"
  app:layout_constraintBottom_toTopOf="@+id/right_bottom"
  app:neopop_button_on_bottom="@id/right_bottom">
  ...content
</club.cred.neopop.PopFrameLayout>

<club.cred.neopop.PopFrameLayout
  ...
  android:id="@+id/right_bottom"
  android:clickable="true"
  app:layout_constraintTop_toTopOf="@id/bottom_space"
  app:neopop_button_on_top="@id/right_top">
  ...content
</club.cred.neopop.PopFrameLayout>

```


## PopLayout All configs
```xml  
<club.cred.neopop.PopFrameLayout    
  android:layout_width="match_parent"    
  android:layout_height="207dp"    
  app:neopop_center_surface_color="@color/white"    
  android:layout_marginHorizontal="24dp"    
  android:layout_marginVertical="54dp">    
  
  <androidx.constraintlayout.widget.ConstraintLayout  
    android:layout_width="match_parent"
    android:layout_height="match_parent">    

    <club.cred.neopop.PopFrameLayout    
      android:id="@+id/topLeft"    
      android:layout_width="84dp"    
      android:layout_height="53dp"    
      app:neopop_center_surface_color="#f00"    
      app:neopop_parent_view_color="@color/white"    
      app:neopop_grandparent_view_color="@color/black"    
      app:layout_constraintTop_toTopOf="parent"    
      app:layout_constraintStart_toStartOf="parent"    
      app:neopop_button_position="top|left"    
      android:clickable="true"/>    

    <club.cred.neopop.PopFrameLayout  
      android:id="@+id/top"    
      android:layout_width="84dp"    
      android:layout_height="53dp"    
      app:neopop_center_surface_color="#f00"    
      app:neopop_parent_view_color="@color/white"    
      app:neopop_grandparent_view_color="@color/black"    
      app:layout_constraintTop_toTopOf="parent"    
      app:layout_constraintStart_toStartOf="parent"    
      app:layout_constraintEnd_toEndOf="parent"    
      app:neopop_button_position="top"    
      android:clickable="true"/>    

    <club.cred.neopop.PopFrameLayout  
      android:id="@+id/topRight"    
      android:layout_width="84dp"    
      android:layout_height="53dp"    
      app:neopop_center_surface_color="#f00"    
      app:neopop_parent_view_color="@color/white"    
      app:neopop_grandparent_view_color="@color/black"    
      app:layout_constraintTop_toTopOf="parent"    
      app:layout_constraintEnd_toEndOf="parent"    
      app:neopop_button_position="top|right"    
      android:clickable="true"/>    

    <club.cred.neopop.PopFrameLayout  
      android:id="@+id/right"    
      android:layout_width="84dp"    
      android:layout_height="53dp"    
      app:neopop_center_surface_color="#f00"    
      app:neopop_parent_view_color="@color/white"    
      app:neopop_grandparent_view_color="@color/black"    
      app:layout_constraintTop_toTopOf="parent"    
      app:layout_constraintBottom_toBottomOf="parent"    
      app:layout_constraintEnd_toEndOf="parent"    
      app:neopop_button_position="right"    
      android:clickable="true"/>    

    <club.cred.neopop.PopFrameLayout  
      android:id="@+id/bottomRight"    
      android:layout_width="84dp"    
      android:layout_height="53dp"    
      app:neopop_center_surface_color="#f00"    
      app:neopop_parent_view_color="@color/white"    
      app:neopop_grandparent_view_color="@color/black"    
      app:layout_constraintBottom_toBottomOf="parent"    
      app:layout_constraintEnd_toEndOf="parent"    
      app:neopop_button_position="bottom|right"    
      android:clickable="true"/>    

     <club.cred.neopop.PopFrameLayout  
       android:id="@+id/bottom"    
       android:layout_width="84dp"    
       android:layout_height="53dp"    
       app:neopop_center_surface_color="#f00"    
       app:neopop_parent_view_color="@color/white"    
       app:neopop_grandparent_view_color="@color/black"    
       app:layout_constraintBottom_toBottomOf="parent"    
       app:layout_constraintEnd_toEndOf="parent"    
       app:layout_constraintStart_toStartOf="parent"    
       app:neopop_button_position="bottom"    
       android:clickable="true"/>    

     <club.cred.neopop.PopFrameLayout  
       android:id="@+id/bottomLeft"    
       android:layout_width="84dp"    
       android:layout_height="53dp"    
       app:neopop_center_surface_color="#f00"    
       app:neopop_parent_view_color="@color/white"    
       app:neopop_grandparent_view_color="@color/black"    
       app:layout_constraintBottom_toBottomOf="parent"    
       app:layout_constraintStart_toStartOf="parent"    
       app:neopop_button_position="bottom|left"    
       android:clickable="true"/>    

     <club.cred.neopop.PopFrameLayout  
       android:id="@+id/left"    
       android:layout_width="84dp"    
       android:layout_height="53dp"    
       app:neopop_center_surface_color="#f00"    
       app:neopop_parent_view_color="@color/white"    
       app:neopop_grandparent_view_color="@color/black"    
       app:layout_constraintBottom_toBottomOf="parent"    
       app:layout_constraintTop_toTopOf="parent"    
       app:layout_constraintStart_toStartOf="parent"    
       app:neopop_button_position="left"    
       android:clickable="true"/>    

     <club.cred.neopop.PopFrameLayout  
       android:id="@+id/center"    
       android:layout_width="84dp"    
       android:layout_height="53dp"    
       app:neopop_center_surface_color="#f00"    
       app:neopop_parent_view_color="@color/white"    
       app:neopop_grandparent_view_color="@color/black"    
       app:layout_constraintBottom_toBottomOf="parent"    
       app:layout_constraintTop_toTopOf="parent"    
       app:layout_constraintEnd_toEndOf="parent"    
       app:layout_constraintStart_toStartOf="parent"    
       app:neopop_button_position="center"    
       android:clickable="true"/>    
	  
  </androidx.constraintlayout.widget.ConstraintLayout> 
	
</club.cred.neopop.PopFrameLayout>  
```  
# TiltLayout
![Tilt Layout](https://user-images.githubusercontent.com/9965653/172598614-0d656dd4-aaae-471f-a6b3-d8b275e9bfab.png "Tilt Layout")


## Non Floating
![Non Floating](https://user-images.githubusercontent.com/9965653/172599904-75d12903-f490-47d6-b8df-39adc9ef058e.png "Non Floating")
![tiltNonFloating](https://user-images.githubusercontent.com/9965653/175874607-e8e10326-1d6d-4b7d-be8f-50cc8f37ee14.gif)
```xml  
<club.cred.neopop.NeoPopQuadFrameLayout  
  ...
  android:clickable="true"    
  app:neopop_parentViewColor="@color/black"    
  app:neopop_black_shadow_height="15dp"    
  app:neopop_black_shadow_top_padding="0dp"    
  app:neopop_card_rotation="18.8"    
  app:neopop_gravity="on_ground"  
  app:neopop_shadow_rotation="32"
  app:neopop_show_shimmer="false"/>  
```  

##  Floating
![Floating](https://user-images.githubusercontent.com/9965653/172599406-6da2d3a4-06ff-4a74-bd6a-988b36a59159.png "Floating")
![tiltFloating](https://user-images.githubusercontent.com/9965653/175874595-86cc0725-df20-4ab3-b432-a6110d4c97c4.gif)

```xml  
<club.cred.neopop.NeoPopQuadFrameLayout  
  ...    
  android:clickable="true"    
  app:neopop_parentViewColor="@color/black"    
  app:neopop_black_shadow_height="15dp"    
  app:neopop_black_shadow_top_padding="0dp"    
  app:neopop_card_rotation="18.8"    
  app:neopop_gravity="on_space"  
  app:neopop_shadow_rotation="32"     
  app:neopop_show_shimmer="false"/>  
```  

## Strokes
![Strokes](https://user-images.githubusercontent.com/9965653/172600281-53eec23d-3596-470e-95ed-dc93ebef82bb.png "Strokes")
![TiltStroked](https://user-images.githubusercontent.com/9965653/175874601-91a27b7d-9e1b-4148-9a26-b9c9245e7a05.gif)
## Shimmer

![shimmer](https://user-images.githubusercontent.com/9965653/175874574-99f209f2-2d14-458b-9f92-959e6aabf112.gif)

```xml  
<club.cred.neopop.NeoPopQuadFrameLayout    
  ...  
  app:neopop_shimmer_duration="5000"  
  app:neopop_top_shimmer_color="#f00"    
  app:neopop_bottom_shimmer_color="#0f0"    
  app:neopop_show_shimmer="true"    
  app:neopop_shadow_rotation="32">  
```  
## All button attributes
| Attribute | Description | Value |  
|--|--|--|  
|`app:neopop_depth`| depth of shadow | dimension |  
|`app:neopop_top_surface_color` or `app:neopop_right_surface_color` or `app:neopop_bottom_surface_color` or `app:neopop_left_surface_olor`| shadow colors | color |  
| `app:neopop_parent_view_color` | immediate ancestor's color | color |  
| `app:neopop_grandparent_view_color` | 2nd level ancestor's color | color |  
| `app:neopop_stroke_color` | layout's stroke colors | color |  
| `app:neopop_surface_color` | card color | style resource |  
| `app:neopop_is_top_surafce_visible` or `app:neopop_is_right_surface_visible` or `app:neopop_is_bottom_surface_visible` or `app:neopop_is_left_surface_visible`| shadow visibility | boolean |  
| `app:neopop_button_position` | position of button in ref to parent view | `top`,`right`, `bottom`,`left`,`center` |  
| `app:neopop_button_on_top` or `app:neopop_button_on_right` or `app:neopop_button_on_bottom` or `app:neopop_button_on_left`| assign reference of button which is on top/right/bottom/left of this button | view id |  
| `app:neopop_shimmer_duration` | total duration of shimmer | seconds in millis |  
| `app:neopop_shimmer_color` | shimmer color | color |  
| `app:neopop_shimmer_width` | shimmer width | dimension |  
| `app:neopop_show_shimmer` | enable shimmer | boolean |  
| `app:neopop_shimmer_repeat_delay` | repeat delay between shimmers | seconds in millis|  
|`app:neopop_shimmer_start_delay` | shimmer start delay | seconds in millis |  
|`app:neopop_animate_on_touch` | use button animator internally to animate | boolean |
## Tilt Specific Attributes
| Attribute | Description | Value |  
|--|--|--|  
| `app:neopop_shadow_color` | bottom plane color | color |  
| `app:neopop_card_rotation` |  |  |  
| `app:neopop_shadow_rotation` |  |  |  
| `app:neopop_gravity` | floating or static | `on_space`, `on_ground`|  
| `app:neopop_top_shimmer_color` | top shimmer color  | color |  
| `app:neopop_bottom_shimmer_color` | bottom shimmer color | color |  
| `app:neopop_black_shadow_top_padding` | | |  
| `app:neopop_black_shadow_height` |  | |  
| `app:neopop_floating_shadow_color` | | color |

## Min SDK

We support a minimum SDK of 21. But the neumorphic components will be rendered appropriately only on devices running SDK version 28 or above.

## Contributing

Pull requests are welcome! We'd love help improving this library. Feel free to browse through open issues to look for things that need work. If you have a feature request or bug, please open a new issue so we can track it.

## Contributors

NeoPop would not have been possible if not for the contributions made by CRED's design and frontend teams. Specifically:
- Rishab Singh Bisht — [Twitter](https://twitter.com/rishabh1310) | [Github](https://github.com/rishabhsinghbisht) | [Linkedin](https://www.linkedin.com/in/rishabh-singh-bisht-b7550938/)
- Nikhil Panju — [Twitter](https://twitter.com/nikhilpanju) | [Github](https://github.com/nikhilpanju) | [Linkedin](https://www.linkedin.com/in/nikhilpanju/)
- Ayush Bansal — [Twitter](https://twitter.com/scorpio002) | [Github](https://github.com/bansalayush) | [Linkedin](https://www.linkedin.com/in/ayush-bansal-81861578/)
- Bhuvanesh Kumar — [Github](https://github.com/bhuvanesh1729) | [Linkedin](https://www.linkedin.com/in/bhuvanesh-kumar-90791a145/)
- Hari Krishna

## License

```  
Copyright 2022 Dreamplug Technologies Private Limited.  
  
Licensed under the Apache License, Version 2.0 (the "License");  
you may not use this file except in compliance with the License.  
You may obtain a copy of the License at  
  
 http://www.apache.org/licenses/LICENSE-2.0  
Unless required by applicable law or agreed to in writing, software  
distributed under the License is distributed on an "AS IS" BASIS,  
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and  
limitations under the License.  
```
