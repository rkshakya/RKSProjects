class Dessert
	def initialize(name, calories)
		@name, @calories = name, calories
	end

	def name
		@name
	end
	
	def calories
		@calories
	end

	def name=(value)
		@name = value
	end

	def calories=(value)
		@calories = value
	end

	def healthy?
		retval = false
		if @calories < 200 
			retval = true
		end
		return retval
	end

	def delicious?
		ret = true
		return ret
	end
end


class JellyBean < Dessert
	def initialize (name, calories, flavor)
		@name, @calories, @flavor = name, calories, flavor
	end

	def flavor
		@flavor
	end

	def flavor=(value)
		@flavor = value
	end

	def delicious?
		ret = true
		if @flavor.casecmp("black licorice") == 0
			ret = false
		end
		return ret
	end
end






