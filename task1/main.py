import math
import random
import numpy
import cPickle
import difflib

def unpickle(filename):
    fo = open(filename, 'rb')
    dict = cPickle.load(fo)
    fo.close()
    return dict

def randomVector(seed):
	random.seed(seed)
	v = []
	for i in xrange(3072):
		v.append(random.random() - 0.5)
	return v

def multiplePlanes(n):
	vArray = []
	for i in xrange(n):
		vArray.append(randomVector(i))
	return vArray
	

# globals (batches and hyperplanes)
g_batch = unpickle("cifar-10-batches-py/data_batch_1")["data"]
g_batch2 = unpickle("cifar-10-batches-py/data_batch_2")["data"]
g_batch3 = unpickle("cifar-10-batches-py/data_batch_3")["data"]
g_batch4 = unpickle("cifar-10-batches-py/data_batch_4")["data"]
g_batch5 = unpickle("cifar-10-batches-py/data_batch_5")["data"]
g_hyperplanes = multiplePlanes(24)

def cosine_similarity(v1,v2): 
    "compute cosine similarity of v1 to v2: (v1 dot v1)/{||v1||*||v2||)"
    sumxx, sumxy, sumyy = 0., 0., 0.
    for i in range(len(v1)):
        x = float(v1[i]); y = float(v2[i])
        sumxx += x*x
        sumyy += y*y
        sumxy += x*y
    return sumxy/math.sqrt(sumxx*sumyy)

def cosine_difference(v1, v2):
	return math.acos(cosine_similarity(v1, v2))
	
def smallestDistanceAndIndex(img, batch):
	distance = 1000000.
	itemIndex = 0
	for i in range(0, len(batch)):
		temporalDistance = cosine_difference(img, batch[i-1])
		if temporalDistance < distance:
			distance = temporalDistance
			itemIndex = i
	returntable = [itemIndex, distance]
	return returntable

def smallestDistance(img, batch):
	return smallestDistanceAndIndex(img, batch)[1]
	
def verbalAboveOrBelow(v1, v2):
	if aboveOrBelow(v1,v2) > 0:
		return "above"
	if aboveOrBelow(v1,v2) < 0:
		return "below"
	return "on the plane"
		
def aboveOrBelow(v1, v2):
	dot = numpy.dot(v1, v2)
	if dot > 0:
		return 1
	if dot < 0:
		return -1
	return 0

# generates the array hash of -1 and 1s	
def makeHash(img):
	hashing = []
	for v in g_hyperplanes:
		dot = aboveOrBelow(img, v)
		hashing.append(dot)
	return hashing	

# makes the array hash to a string format
def makeStringHash(img):
	_h = makeHash(img)
	_hStr = ''.join(str(x) for x in _h)
	return _hStr
		
def batchMapping(batch):
	kvStorage = dict()
	tasd = 0
	for img in batch:
		_hStr = makeStringHash(img)
		if _hStr not in kvStorage:
			kvStorage[_hStr] = list()
		kvStorage[_hStr].append(img)
	return kvStorage	

def choose_100(batch):
	array = []
	rand = random.randint(0, len(g_batch)-101)
	for i in range((rand+1),(rand+100)):
			array.append((g_batch[i]))	
	return array

#print(smallestDistanceinBatch(batch[777], batch))
#print(verbalAboveOrBelow(randomVector(), g_batch[random.randint(0,10000)]))
#testing = makeHash(g_batch[333])
#print(testing)
#string = ''.join(str(x) for x in testing)
#print(string)
#dictionary = batchMapping(g_batch, hyperplanes)
#temp = dictionary[string]
#print(dictionary.keys())
#print(len(dictionary.keys()))
#print(len(temp))

# Problem is when there's hash that's not in the dict
# TODO Thats why we need to find out the closest possible hash 
def difflib_findClosest(hashString, dictionary):
	closest = difflib.get_close_matches(hashString[::-1], dictionary.keys(), 1)
	print("closests " +str(closest))
	return closest
#B LSH (locality-sensitive-hashing) Forrest
def findClosest(hashString, dictionary):
	#ratio() accurate
	#quick_ratio() approximation
	#real_quick_ratio() even larger approximation
	sm = difflib.SequenceMatcher
	largest_ratio = 0. 
	k = ""
	for key in dictionary.keys():
		dif = sm(None, hashString, key)
		r = dif.quick_ratio();
		k = key
		if(r > largest_ratio):
			largest_ratio = r;
		# 2 % 
		if(largest_ratio > 0.97): 
			k = key;
			break
	print k
	return dictionary.get(k)

def createHashDictionary(batch):
	return batchMapping(batch)
def exactSmallestDistance(img, batch):
	return smallestDistance(img, batch)
def approximateSmallestDistance(img, dictionary, fallbackBatch):
	hashString = makeStringHash(img)
	print(hashString)
	hashPool = dictionary.get(hashString)
	if(hashPool is None):
		return smallestDistance(img, fallbackBatch);
		# hashPool = findClosest(hashString, dictionary)
	# print len(hashPool)
	return smallestDistance(img, hashPool)

# classic chi chi
#chosenones = choose_100(g_batch)
#print(exactDistances(chosenones))
#print(approximateDistances(chosenones))

def discrepencies():
	ds1 = []
	ds2 = []
	sum1 = 0
	sum2 = 0
	searchables = choose_100(g_batch2)
	batchDict = batchMapping(choose_100(g_batch2));
	for i in range(0,99):
		print(i)
		chos = choose_100(g_batch)
		chosDict = createHashDictionary(chos)
		eDist = exactSmallestDistance(searchables[i], chos)
		aDist = approximateSmallestDistance(searchables[i], chosDict, chos)
		ds1.append(eDist)
		ds2.append(aDist)
		print "edist :"+ str(eDist)+" adist: "+str(aDist)		
	for i in range(0, len(ds1)-1):
		sum1 += ds1[i]
		sum2 += ds2[i]
	return abs((sum1-sum2) / len(ds1))

print("error : "+str(discrepencies()))