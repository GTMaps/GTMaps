rooomPts in each hallway folder consists of room's 4 corner's coordinate and the door's coordinate. 

For exmaple:
130
4.0,4.0
37.0,4.0
38.0,53.0
4.0,53.0
38.0,53.0
The "130" in the first row is the room number.
The next 4 rows are the x,y coordinates for the room's corners. The 1st coordinate represents the top-left corner. The 2nd coordinate represents the top-right corner. The 3rd coordinate represnets the down-right corner. The 4th coordinate represents the down-left corner. 
The 5th coordinate represents the door of the room.


**Note**
1. The coordniates have been adjusted manually to be as accurate as possible, but potentially there would still be some coordinate that are not aligned exactly (off by 1-4 pixels). A possible fix for this problem would be: Take the average of y/x coordinates of doors on a hallway upon programming.
2. At this point, each room only has 1 door. Would be further updated.
3. Hallway folders don't represent real hallways. Please rearrange rooms when creating a hallway in program.
