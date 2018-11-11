# FireWallProblem
## Run Instruction
1. Download and decompress the folder.
2. (Optional) If you want to costomize your testcases. You can add your rules test cases in rules.csv file. Also, you can add packets in main function.
3. Open the terminal and change directory to the root directory
4. Run ```javac Main.java``` to compile
5. Run ```java Main``` to run the filewall project

## Algorithmic choices 

At the beginning I came up with a straight-forward solution of nested HashMap, with the outer key being the string composed of packet direction and protocol, and value being another HashMap of port (int) - IP (String) pairs. This data structure will accomplish O(1) time for checking whether a packet can be accepted, but the space complexity will be very high (4*2^16*2^32), and it gets out of memory error very soon. Therefore, I further optimize the data structure by using trie tree, instead of string, to store IP address. For each port number, there is a dummy root node mapped with it, and following the root there are four level of tree node with value from 0 to 255, and each level there are up to 255^i nodes (i being the level, from 1 to 4). The trie tree structure saves space significantly (with time complexity still being constant), but it still gets out of memory error with I try to load rule like “inbound,tcp,53,0.0.0.0-0.30.255.255”. To solve this, I have to either put rule map on hard disk instead of RAM, or further optimize the current data structure and algorithm (which I will discuss in the next section). 

## Test plan

### 1. Unit test.
#### 1.1 For Constructor:
I need to test two parts for this section. First one is to read cvs file correctly, and I test that one by manually check if I read line by line correctly. The second one is to store the rules correctly; due to the time limit, I test it by using the IntelliJ Debugger to watch it step by step to see if it works as I expected.

#### 1.2 For Accept_packet function:
Due to the time limit, I didn't use JUnit framework; instead, I write some testcases in the Main class to simulate the assert function. The test cases include:
 - The sample test cases given in the instruction pdf.
 - The common cases.
 - The boundary cases.

### 2. Performance test.
#### 2.1 Space Complexity
Since the heap's space is limited and the space complexity is based on how many rules we have, I tested the performance of space complexity on change the size of test rule's in cvs file. The result is that when I try "inbound, tcp, 53, 0.0.0.0-0.30.255.255" or larger port/IP range, it will be out of memory.

#### 2.2 Time Complexity
The time complexity for accept_packet is O(1) so I haven't tested it yet. The time in constructor in the CSV file will be the main cost of time.

## Further refinements or optimizations 
### 1. Testing:
 - I will use unit test framework(JUnit) to further test this algorithm.
 - I will generate a larger data set (eg. 500K- 1M) to test this algorithm.

### 2. Algorithm:
 - Building upon the current data structure, I would try to use BitSet to further optimize the space, or at least try to user BitSet to represent IP address.
 - Besides the current data structure and algorithm, which simply lists every combination of port and IP address, I would try to come up with another algorithm which stores permitted IP range for each port. With that algorithm, I will need to perform interval merging each time I read in a new rule which has some overlaps with existing ones, and that way the checking valid function will run on O(lgn) time to do binary search.
 - I would also try to use multithread for reading in firewall rule files and checking a certain packet.

### 3. Analysis of the firewall rules:
 - Input rules should be further analyzed to see if any pattern exists. For example, if a certain range of IP and port is very popular, maybe we could load the most frequently visited area into RAM and leave other data on hard disk.

## Team preference
1. platform team
2. policy team
3. data team

### At the end
- Thanks for your time to review my code, if you have some concerns or want to further discuss some details just let me know.


