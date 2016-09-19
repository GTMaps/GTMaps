# ************************** #
# Quick map search prototype #
# ******* Tim Trent ******** #
# *******  GT Maps  ******** #
# ************************** #

import cv2
import Queue
import sys

building = []
red = (0,0,255)

# This is a room. It has a room number (title) which is used for
# comparison to other rooms.
class room:
    def __init__(self,num='',coord=None):
        global building
        self.number = num
        building.append(num) # implementation. check for non-existent rooms
        self.center = coord
    def __eq__(self, other):
        if isinstance(other, self.__class__):
            return other.number.lower() == self.number.lower()
        else:
            return False
    def __str__(self):
        return 'Room number: %s'%(self.number)

# This is a connection between two hallways. Knows which two hallways are
# connected and will inform each hallway that it exists when created.
class junction:
    def __init__(self,halls=[]):
        self.halls = halls
        for h in halls:
            h.junctions.append(self)
    def __str__(self):
        hallstr = [s.__str__() for s in self.halls]
        return 'Connection between halls %s'%(hallstr)

# This is a hallway. It has a list of rooms (both side "A" and "B", see below
# for details on direction) as well as the associated junctions and a name.
class hallway:
    def __init__(self, name = None, coord = None):
        self.roomsA = []
        self.roomsB = []
        self.junctions = []
        self.name = name
        # This is a mapping for what side of the hall roomsA/B are on
        # based on the previous hallway {last hallway : {a : left, b : right}}
        self.orient = {self:{"a":"right", "b":"left"}}
        self.center = coord
    def __str__(self):
        if self.name == None:
            arooms = [r.__str__() for r in self.roomsA]
            brooms = [r.__str__() for r in self.roomsB]
            return 'Hallway containing rooms %s and %s'%(arooms, brooms)
        else:
            return "Hallway %s"%(self.name)

# Node subclass to make graph search easier
class node:
    def __init__(self, hall, previous):
        self.hall = hall
        self.prev = previous

#----------------------------Test Code Below-----------------------------------#

# General graph search taking in a type of struc, the starting hallway,
# and the requested room
def roomSearch(struc, start, r):
    currNode = node(start, None)
    found = False
    visited = []
    while not found:
        if r in currNode.hall.roomsA or r in currNode.hall.roomsB:
            found = True
            break
    
        visited.append(currNode)
        success = []
        for h in currNode.hall.junctions:
            for i in range(len(h.halls)):
                if not h.halls[i] == currNode.hall:
                    success.append(h.halls[i])
        for state in success:
            struc.put(node(state,currNode))
            
        if not struc.empty():
            next = struc.get()
            while next in visited and not struc.empty():
                next = struc.get()
            currNode = next
        else:
            visited = []
        
    visited = None
    ret = list()
    while not currNode.hall == start:
        ret.append(currNode.hall)
        currNode = currNode.prev
    ret.append(start)
    ret.reverse()
    return ret

if __name__ == "__main__":
    # Catch the command line argument or exit if none is provided.
    if len(sys.argv) < 2:
        print "usage: python searchTest.py [DESIRED ROOM]"
        exit(0)
    requested_room = str(sys.argv[1])

    # Create all the hallways for the test map
    h0 = hallway("Entry 1", (278,25))
    h1 = hallway("H1",(278,267))
    h2 = hallway("H2", (119,197))
    h3 = hallway("H3", (114, 309))
    h4 = hallway("H4", (375,345))
    h5 = hallway("H5", (482,215))
    h6 = hallway("Entry 2", (485, 25))
    
    # Make appropriate junctions between hallways
    junction([h0,h1])
    junction([h1,h2])
    junction([h1,h3])
    junction([h1,h4])
    junction([h4,h5])
    junction([h6,h5])
    
    # Populate the appropriate orientation for each junction
    h1.orient[h0] = {"a":"right", "b":"left"}
    h1.orient[h4] = {"a":"left", "b":"right"}
    h2.orient[h1] = {"a":"left", "b":"right"}
    h3.orient[h1] = {"a":"left", "b":"right"}
    h4.orient[h1] = {"a":"right", "b":"left"}
    h4.orient[h5] = {"a":"left", "b":"right"}
    h5.orient[h4] = {"a":"left", "b":"right"}
    h5.orient[h6] = {"a":"right", "b":"left"}
    
    #Populate the rooms list of each hallway with the appropriate rooms
    h1.roomsA.append(h0) # *Store hallways as rooms to make directions easier*
    h1.roomsB.append(h0)
    h1.roomsA.append(room("A1"))
    h1.roomsA.append(h2)
    h1.roomsA.append(h3)
    h1.roomsB = [room("O%d"%(i)) for i in range(4,9)]
    h1.roomsB.append(h4)
    h2.roomsB.append(room("A1"))
    h2.roomsA = [room("O%d"%(i)) for i in range(1,4)]
    h2.roomsB.append(h1)
    h2.roomsA.append(h1)
    h3.roomsA = [room("C%d"%(i)) for i in range(1,3)]
    h3.roomsB.append(h1)
    h3.roomsA.append(h1)
    h4.roomsB.append(h1)
    h4.roomsB.append(h5)
    h5.roomsB.append(h6)
    h5.roomsA = [room("O%d"%(i)) for i in range(9,14)]
    h5.roomsA[-1].center = (30, 78)
    h5.roomsA.append(h6)
    h5.roomsA.reverse()
    h5.roomsA.append(h4)
    
    # Catch non-existent room
    if not requested_room in building:
        print "This room does not exist, please try again."
        exit(0)
    request = room(requested_room)
    
    # Create a queue, then search starting at hallway 0 (entrance)
    # for the directions
    q = Queue.Queue()
    res = roomSearch(q, h6, request)
    
    # Iterate over search results, finding the last room (or hall) before a turn
    # and print out directions
    for i in range(1, len(res) - 1):
        temp = res[i]
        roomlist,dir = (temp.roomsA,"a") if res[i+1] in temp.roomsA else \
                            (temp.roomsB,"b")
        junctionIndex = roomlist.index(res[i+1])
        print "Walk %s and turn %s"%("past %s"%(roomlist[junctionIndex - 1]) \
                    if len(roomlist) > 2 else "to the end of this hall", \
                    temp.orient[res[i-1]][dir])

    # Print out notification of the proper side of the hallway to
    # expect the room
    temp = res[-1]
    print "%s will be on this hall, on your %s."%(request, \
                temp.orient[res[-2]]["a"] if request in temp.roomsA else \
                temp.orient[res[-2]]["b"])
                
    if not str(raw_input("Would you like to try a line (y/n)? ")).lower() == "y":
        exit(0)
    
    # Drawing line
    im = cv2.imread("test_map.png")
    for i in range(len(res)-1):
        thisx,thisy = res[i].center
        nextx, nexty = res[i+1].center
        if (abs(thisx - nextx) > abs(thisy - nexty)):
            cv2.line(im, (thisx,thisy), (thisx, nexty), red)
            cv2.line(im, (thisx,nexty), (nextx,nexty), red)
        else:
            cv2.line(im, (thisx,thisy), (nextx,thisy), red)
            cv2.line(im, (nextx,thisy), (nextx, nexty), red)
    if requested_room == "O13":
        thisx,thisy = res[-1].center
        nextx,nexty = (408,78)
        cv2.line(im, res[-1].center, (thisx,nexty), red)
        cv2.line(im, (thisx,nexty), (nextx,nexty), red)
    cv2.imshow("Path",im)
    cv2.waitKey(0)