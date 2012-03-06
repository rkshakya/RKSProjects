class Class
	def attr_accessor_with_history(attr_name)
		attr_name = attr_name.to_s
		attr_reader attr_name
		attr_reader attr_name+"_history"

		class_eval  %Q{		
			def #{attr_name}= (val)
				@#{attr_name} = val
                                if @#{attr_name}_history
                                	@#{attr_name}_history.push(val)
				else
					@#{attr_name}_history = Array.new()
                                        @#{attr_name}_history.push(nil)
					@#{attr_name}_history.push(val)  
                                end       
			end			
			
			def #{attr_name+"_history"}		
				return @#{attr_name}_history
                               
			end
		}
	end
end

class Foo
	attr_accessor_with_history :bar
	
end




